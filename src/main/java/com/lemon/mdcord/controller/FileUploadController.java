package com.lemon.mdcord.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.Random;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 10;

    public static String generate() {
        Random random = new SecureRandom();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }

    @PostMapping("/fileUpload")
    public ResponseEntity<?> uploadFile(
            @RequestPart(value = "files", required = false) MultipartFile[] files,
            @RequestParam(value = "channelId", required = false) String channelId) {
        System.err.println("파일업로드 시작");
        System.err.println("files : " + files);
        System.err.println("files.length : " + files.length);
        System.err.println("ChannelId : " + channelId);

        for (MultipartFile file : files) {
            System.err.println("-------------------");
            System.err.println(file.getOriginalFilename());
            System.err.println(file.getResource());
            System.err.println(file.getContentType());
            System.err.println("-------------------");

            String fileName = file.getOriginalFilename();
            Path targetDir = Paths.get("attach-file", channelId); // 업로드될 경로 attach-file은 properties로 빼기

            try {
                Files.createDirectories(targetDir); // 경로가 없으면 생성해준다
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.err.println("fileName : " + fileName);
            String randomFileName = generate();
            String ext = fileName.substring(fileName.lastIndexOf("."));
            Path targetFile = targetDir.resolve(randomFileName + ext); // fileName은 Random String 10자리

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(
                        inputStream,
                        targetFile,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.ok().build();
    }
}
