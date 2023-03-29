package com.lemon.mdcord.service.chat;

import com.lemon.mdcord.common.exception.AttachFileNotFoundException;
import com.lemon.mdcord.common.exception.AttachFileNotMatchedImageFileExt;
import com.lemon.mdcord.common.exception.InvalidChannelIdException;
import com.lemon.mdcord.domain.chat.AttachFile;
import com.lemon.mdcord.repository.AttachFileRepository;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.repository.ChannelMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JpaAttachFileService implements AttachFileService {

    private final AttachFileRepository attachFileRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final ChannelListRepository channelListRepository;
    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final String UPPER_DIRECTORY = "attach-file";
    private static final List<String> FILE_EXTS = new ArrayList<>(List.of(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg"));

    @Override
    public byte[] getImageFileInfo(Long channelId, String fileName) {
        validateImageFileInfo(fileName);

        String filePath = getFilePath(channelId);
        log.debug("current project path : {}", ROOT_PATH);
        log.debug("file path : {}", filePath);

        byte[] imageBytes = null;
        try {
            Path path = Paths.get(filePath, fileName);
            imageBytes = Files.readAllBytes(path);
        }
        catch(Exception e) {
            log.error("channel ID : {}, file name : {}", channelId, fileName);
            log.error("getClass : {}", e.getClass());
            log.error("getLocalizedMessage : {}", e.getLocalizedMessage());
            log.error("getMessage : {}", e.getMessage());
            log.error("getCause : {}", e.getCause());
        }

        return imageBytes;
    }

    @Override
    public Pair<byte[], String> getAttachFile(Long channelId, Long attachFileId) {
        validateJoinedChannelList(channelId);

        AttachFile attachFile = attachFileRepository.findById(attachFileId).orElseThrow(() -> new AttachFileNotFoundException(attachFileId));

        String realFileName = attachFile.getRealFileName() + attachFile.getFileExt();
        String filePath = getFilePath(channelId);
        log.debug("current project path : {}", ROOT_PATH);
        log.debug("file path : {}", filePath);

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

        String originFileName = attachFile.getOriginFileName();
        Pair<byte[], String> result = Pair.of(imageBytes, originFileName);

        return result;
    }

    private String getFilePath(Long channelId) {
        return ROOT_PATH + "\\" + UPPER_DIRECTORY + "\\" + channelId + "\\";
    }

    private void validateJoinedChannelList(Long channelId) {
        String currentMemberId = getAuthentication().getName();
        Set<Long> joinedRootChannelIds = channelMemberRepository.findByMemberId(currentMemberId).stream()
                .map(o -> o.getChannelList().getId())
                .collect(Collectors.toSet());
        List<Long> joinedChannelIds = channelListRepository.findByParentIdIn(joinedRootChannelIds).stream()
                .map(o -> o.getId())
                .collect(Collectors.toList());
        if(!joinedChannelIds.contains(channelId)) throw new InvalidChannelIdException(channelId);
    }

    private void validateImageFileInfo(String fileName) {
        AttachFile attachFile = attachFileRepository.findByRealFileName(fileName).orElseThrow(() -> new AttachFileNotFoundException(fileName));
        if(!FILE_EXTS.contains(attachFile.getFileExt())) throw new AttachFileNotMatchedImageFileExt(attachFile.getFileExt());
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
