package com.lemon.mdcord.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ImageController {

    @GetMapping(value = "/channels/{channelId}/attach-file/{imageName}")
    public byte[] getImage(
            @PathVariable("channelId") Long channelId,
            @PathVariable("imageName") String imageName) throws IOException {
        // src="http://localhost:9000/channels/6/attach-file/V1BjqC0Fhr.png"
        String rootPath = System.getProperty("user.dir");
        log.info("현재 프로젝트의 경로 : " + rootPath);
        log.info("imageName : " + rootPath);

        Path path = Paths.get(rootPath + "\\attach-file\\" + channelId + "\\", imageName);
        byte[] imageBytes = Files.readAllBytes(path);
        return imageBytes;
    }
}