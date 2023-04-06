package com.lemon.mdcord.service.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.lemon.mdcord.common.exception.ChannelNotFoundException;
import com.lemon.mdcord.common.exception.ChatNotFoundException;
import com.lemon.mdcord.common.exception.MemberNotFoundException;
import com.lemon.mdcord.common.exception.RegistAttachFileException;
import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.domain.chat.AttachFile;
import com.lemon.mdcord.domain.chat.ChannelChat;
import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.chat.ChannelChatListResponse;
import com.lemon.mdcord.dto.socket.ChatCreateRequest;
import com.lemon.mdcord.dto.socket.MessageType;
import com.lemon.mdcord.repository.AttachFileRepository;
import com.lemon.mdcord.repository.ChannelChatRepository;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.repository.MemberRepository;
import com.lemon.mdcord.service.MessageTypeInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JpaChannelChatService implements ChannelChatService, MessageTypeInterface {

    private final ChannelChatRepository channelChatRepository;
    private final ChannelListRepository channelListRepository;
    private final MemberRepository memberRepository;
    private final AttachFileRepository attachFileRepository;
    private final ObjectMapper objectMapper;

    // TODO - property 분리 필요
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int FILENAME_LENGTH = 10;
    private static final String UPPER_DIRECTORY = "attach-file";

    @Override
    public boolean support(String messageType) {
        return MessageType.SEND_MESSAGE.name().equals(messageType)
                || MessageType.EDIT_MESSAGE.name().equals(messageType)
                || MessageType.DELETE_MESSAGE.name().equals(messageType);
    }

    @Override
    public void handle(String messageType, String payload, Map<Long, List<WebSocketSession>> channelMap) throws IOException {
        ChatCreateRequest request = new Gson().fromJson(payload, ChatCreateRequest.class);

        ChannelChat result = null;
        if(MessageType.SEND_MESSAGE.name().equals(messageType)) {
            result = this.createChannelChat(request);
        }
        else if(MessageType.EDIT_MESSAGE.name().equals(messageType)) {
            result = this.changeChannelChatInfo(request);
        }
        else if(MessageType.DELETE_MESSAGE.name().equals(messageType)) {
            result = this.deleteChannelChatInfo(request);
        }

        // 메시지 소켓 통신
        List<WebSocketSession> webSocketSessions = channelMap.get(request.getChannelId());
        TextMessage modifiedMessage = modifiedMessage(payload, result);
        for(WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(modifiedMessage);
        }
    }

    @Override
    public ChannelChat createChannelChat(ChatCreateRequest request) {
        String memberId = request.getMemberId();
        Long channelId = request.getChannelId();
        ChannelList channelList = channelListRepository.findById(channelId).orElseThrow(() -> new ChannelNotFoundException(channelId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        // TODO - getAuthentication() 왜 안꺼내지는지?
        ChannelChat channelChat = ChannelChat.builder()
                .channelList(channelList)
                .member(member)
                .content(request.getContent())
                .fileYn(request.getFileYn())
                .createBy(request.getMemberId())
                .build();

        return channelChatRepository.save(channelChat);
    }

    @Override
    public List<AttachFile> createAttachFile(MultipartFile[] files, Long chatId) {
        log.debug("파일업로드 시작");
        log.debug("files : {}", files);
        log.debug("files.length : {}", files.length);
        log.debug("ChannelId : {}", chatId);

        ChannelChat targetChat = channelChatRepository.findById(chatId).orElseThrow(() -> new ChatNotFoundException(chatId));
        List<AttachFile> result = new ArrayList<>();

        for (MultipartFile file : files) {
            log.debug("-------------------");
            log.debug("Original file name : {}", file.getOriginalFilename());
            log.debug("Resource : {}", file.getResource());
            log.debug("Content type : {}", file.getContentType());
            log.debug("-------------------");

            Path targetDir = Paths.get(UPPER_DIRECTORY, String.valueOf(targetChat.getChannelList().getId()));

            try {
                Files.createDirectories(targetDir); // 경로가 없으면 생성해준다
            } catch (Exception e) {
                String msg = "디렉토리 생성 실패 - Exception";
                log.error(msg);
                log.error("Path : {}", targetDir);
                log.error("getClass : {}", e.getClass());
                log.error("getLocalizedMessage : {}", e.getLocalizedMessage());
                log.error("getMessage : {}", e.getMessage());
                throw new RegistAttachFileException(msg, targetDir);
            }

            String randomFileName = fileNameGenerator();
            String ext = getFileExt(file);
            Path targetFile = targetDir.resolve(randomFileName + ext);

            AttachFile attachFile = AttachFile.builder()
                    .channelChat(targetChat)
                    .originFileName(file.getOriginalFilename())
                    .realFileName(randomFileName)
                    .fileExt(ext)
                    .fileSize(file.getSize())
                    .createBy(getAuthentication().getName())
                    .build();

            try (InputStream inputStream = file.getInputStream()) {
                insertAttachFile(result, attachFile);
                Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                String msg = "파일 생성 실패 - Exception";
                log.error(msg);
                log.error("Path : {}", targetFile);
                log.error("getClass : {}", e.getClass());
                log.error("getLocalizedMessage : {}", e.getLocalizedMessage());
                log.error("getMessage : {}", e.getMessage());
                throw new RegistAttachFileException(msg, targetDir);
            }
        }
        return result;
    }

    @Override
    public ChannelChat changeChannelChatInfo(ChatCreateRequest request) {
        Long chatId = request.getChatId();
        ChannelChat channelChat = channelChatRepository.findById(chatId).orElseThrow(() -> new ChatNotFoundException(chatId));
        channelChat.changeChannelChatInfo(request.getContent(), request.getMemberId());
        return channelChat;
    }

    @Override
    public Page<ChannelChatListResponse> getChannelChatList(Long channelId, Long chatId, Pageable pageable) {
        ChannelList channelList = channelListRepository.findById(channelId).orElseThrow(() -> new ChannelNotFoundException(channelId));

        if(chatId != null) {
            return channelChatRepository.findByChannelListAndIdLessThan(channelList, chatId, pageable)
                    .map(ChannelChatListResponse::new);
        }

        return channelChatRepository.findByChannelList(channelList, pageable)
                .map(ChannelChatListResponse::new);
    }

    @Override
    public ChannelChat deleteChannelChatInfo(ChatCreateRequest request) {
        Long chatId = request.getChatId();
        ChannelChat channelChat = channelChatRepository.findById(chatId).orElseThrow(() -> new ChatNotFoundException(chatId));
        channelChat.deleteChannelChatInfo(request.getMemberId());
        return channelChat;
    }

    /**
     * 파일 정보 DB 저장 및 반환될 리스트에 추가
     * 
     * @param result
     * @param attachFile
     */
    private void insertAttachFile(List<AttachFile> result, AttachFile attachFile) {
        AttachFile savedFile = attachFileRepository.save(attachFile);
        result.add(savedFile);
    }

    /**
     * 로그인 사용자 정보 가져오기
     * 
     * @return
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 파일 확장자 추출
     *
     * @param file
     * @return
     */
    private static String getFileExt(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        log.debug("originalFileName : {}", originalFileName);
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 랜덤한 10글자 파일명 생성
     *
     * @return
     */
    public static String fileNameGenerator() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < FILENAME_LENGTH; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(CHARACTERS.length()); // 0부터 CHARACTERS.length() 미만 구간의 랜덤 값
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }

    /**
     * message 정보에 채팅 ID, 생성일시 데이터 추가
     *
     * @param payload
     * @param channelChat
     * @return
     * @throws JsonProcessingException
     */
    private TextMessage modifiedMessage(String payload, ChannelChat channelChat) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        ((ObjectNode)jsonNode).put("chatId", channelChat.getId());
        ((ObjectNode)jsonNode).put("channelName", channelChat.getChannelList().getName());
        ((ObjectNode)jsonNode).put("createDate", channelChat.getCreateDate().toString());

        return new TextMessage(objectMapper.writeValueAsString(jsonNode));
    }

}
