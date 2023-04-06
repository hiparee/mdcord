package com.lemon.mdcord.service.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.lemon.mdcord.common.exception.AttachFileNotFoundException;
import com.lemon.mdcord.common.exception.AttachFileNotMatchedImageFileExt;
import com.lemon.mdcord.common.exception.InvalidChannelIdException;
import com.lemon.mdcord.domain.chat.AttachFile;
import com.lemon.mdcord.dto.chat.ChatAttachFileResponse;
import com.lemon.mdcord.dto.socket.ChatCreateRequest;
import com.lemon.mdcord.dto.socket.MessageType;
import com.lemon.mdcord.repository.AttachFileRepository;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.repository.ChannelMemberRepository;
import com.lemon.mdcord.service.MessageTypeInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JpaAttachFileService implements AttachFileService, MessageTypeInterface {

    private final AttachFileRepository attachFileRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelListRepository channelListRepository;
    private final ObjectMapper objectMapper;
    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final String UPPER_DIRECTORY = "attach-file";
    private static final List<String> FILE_EXTS = new ArrayList<>(List.of(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg"));

    @Override
    public boolean support(String messageType) {
        return MessageType.UPLOAD_FILE.name().equals(messageType);
    }

    @Override
    public void handle(String messageType, String payload, Map<Long, List<WebSocketSession>> channelMap) throws IOException {
        ChatCreateRequest request = new Gson().fromJson(payload, ChatCreateRequest.class);

        // 메시지 소켓 통신
        List<WebSocketSession> webSocketSessions = channelMap.get(request.getChannelId());
        TextMessage modifiedMessage = modifiedMessage(payload, request.getFileList());
        for(WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(modifiedMessage);
        }
    }

    @Override
    public byte[] getImageFileInfo(Long channelId, String fileName) {
        if(!fileName.contains(".")) throw new AttachFileNotFoundException(fileName);
        validateImageFileInfo(fileName);

        String filePath = getFilePath(channelId);
        String fileExt = fileName.split("\\.")[1];

        ByteArrayOutputStream os = null;
        byte[] imageBytes = null;
        try {
            Path path = Paths.get(filePath, fileName);
            imageBytes = Files.readAllBytes(path);
            if(imageBytes != null && fileExt.equals("gif")) {
                return imageBytes;
            }

            File file = new File(path.toString());
            BufferedImage bi = ImageIO.read(file);
            os = new ByteArrayOutputStream();
            setThumbnailOptions(bi.getWidth(), bi.getHeight(), fileExt, os, imageBytes);
        }
        catch(Exception e) {
            log.error("channel ID : {}, file name : {}", channelId, fileName);
            log.error("getClass : {}", e.getClass());
            log.error("getLocalizedMessage : {}", e.getLocalizedMessage());
            log.error("getMessage : {}", e.getMessage());
            log.error("getCause : {}", e.getCause());
        }

        if(imageBytes == null || os == null) {
            log.error("file name : {}", fileName);
            log.error("current project path : {}", ROOT_PATH);
            log.error("file path : {}", filePath);
        }

        return os.toByteArray();
    }

    @Override
    public Pair<byte[], String> getAttachFile(Long channelId, Long attachFileId) {
        validateJoinedChannelList(channelId);

        AttachFile attachFile = attachFileRepository.findById(attachFileId).orElseThrow(() -> new AttachFileNotFoundException(attachFileId));

        String realFileName = attachFile.getRealFileName() + attachFile.getFileExt();
        String filePath = getFilePath(channelId);

        byte[] imageBytes = null;
        try {
            Path path = Paths.get(filePath, realFileName);
            imageBytes = Files.readAllBytes(path);
        }
        catch(Exception e) {
            log.error("channel ID : {}, file name : {}", channelId, realFileName);
            log.error("getClass : {}", e.getClass());
            log.error("getLocalizedMessage : {}", e.getLocalizedMessage());
            log.error("getMessage : {}", e.getMessage());
            log.error("getCause : {}", e.getCause());
        }

        if(imageBytes == null) {
            log.error("current project path : {}", ROOT_PATH);
            log.error("file path : {}", filePath);
        }

        String originFileName = attachFile.getOriginFileName();
        Pair<byte[], String> result = Pair.of(imageBytes, originFileName);

        return result;
    }

    private void setThumbnailOptions(int fileWidth, int fileHeight, String fileExt, ByteArrayOutputStream os, byte[] imageBytes) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        if( fileWidth > 450 ) {
            float scale = 450.0f / fileWidth; // 450 : 클라이언트에서 정한 파일썸네일 가로 최대 길이
            Thumbnails.of(inputStream)
                    .outputFormat(fileExt)
                    .forceSize((int)(fileWidth * scale), (int)(fileHeight * scale))
                    .toOutputStream(os);
        }
        else {
            Thumbnails.of(inputStream)
                    .scale(1)
                    .outputFormat(fileExt)
                    .toOutputStream(os);
        }
    }

    private String getFilePath(Long channelId) {
        return UPPER_DIRECTORY + "/" + channelId + "/";
    }

    private void validateJoinedChannelList(Long channelId) {
        String currentMemberId = getAuthentication().getName();
        Set<Long> joinedRootChannelIds = channelMemberRepository.findByMemberId(currentMemberId).stream()
                .map(o -> o.getChannelList().getId())
                .collect(Collectors.toSet());
        Set<Long> joinedParentChannelIds = channelListRepository.findByParentIdIn(joinedRootChannelIds).stream()
                .map(o -> o.getId())
                .collect(Collectors.toSet());
        Set<Long> joinedChannelIds = channelListRepository.findByParentIdIn(joinedParentChannelIds).stream()
                .map(o -> o.getId())
                .collect(Collectors.toSet());
        if(!joinedChannelIds.contains(channelId)) throw new InvalidChannelIdException(channelId);
    }

    private void validateImageFileInfo(String fileName) {
        if(fileName.matches("/[a-z0-9]*.[a-z]")) throw new AttachFileNotFoundException(fileName);
        String[] splitFileName = fileName.split("\\.");
        AttachFile attachFile = attachFileRepository.findByRealFileName(splitFileName[0]).orElseThrow(() -> new AttachFileNotFoundException(fileName));
        if(!FILE_EXTS.contains(attachFile.getFileExt())) throw new AttachFileNotMatchedImageFileExt(attachFile.getFileExt());
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * message 정보에 첨부 파일 목록 추가
     *
     * @param payload
     * @param fileList
     * @return
     * @throws JsonProcessingException
     */
    private TextMessage modifiedMessage(String payload, List<ChatAttachFileResponse> fileList) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        ((ObjectNode)jsonNode).put("fileList", new Gson().toJson(fileList));

        return new TextMessage(objectMapper.writeValueAsString(jsonNode));
    }

}
