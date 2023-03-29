package com.lemon.mdcord.service.chat;

import com.lemon.mdcord.common.exception.AttachFileNotFoundException;
import com.lemon.mdcord.common.exception.AttachFileNotMatchedImageFileExt;
import com.lemon.mdcord.domain.chat.AttachFile;
import com.lemon.mdcord.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JpaAttachFileService implements AttachFileService {

    private final AttachFileRepository attachFileRepository;
    private static final String rootPath = System.getProperty("user.dir");
    private static final List<String> fileExts = new ArrayList<>(List.of(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".svg"));

    @Override
    public byte[] getImageFileInfo(Long channelId, String fileName) {
        validateImageFileInfo(fileName);

        String filePath = rootPath + "\\attach-file\\" + channelId + "\\";
        log.debug("current project path : {}", rootPath);
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

    private void validateImageFileInfo(String fileName) {
        AttachFile attachFile = attachFileRepository.findByRealFileName(fileName).orElseThrow(() -> new AttachFileNotFoundException(fileName));
        if(!fileExts.contains(attachFile.getFileExt())) throw new AttachFileNotMatchedImageFileExt(attachFile.getFileExt());
    }

}
