package com.lemon.mdcord.controller;

import com.lemon.mdcord.service.chat.AttachFileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
public class AttachFileController {

    private final AttachFileService attachFileService;

    @Operation(summary = "이미지 파일 정보 조회", description = "이미지 파일 정보 조회 API")
    @GetMapping(value = "/channels/{channelId}/image-file/{imageName}")
    public byte[] getImage(
            @PathVariable("channelId") Long channelId,
            @PathVariable("imageName") String fileName) {
        return attachFileService.getImageFileInfo(channelId, fileName);
    }
    
    @Operation(summary = "파일 다운로드", description = "파일 다운로드 API")
    @GetMapping(value = "/channels/{channelId}/attach-file/{attachFileId}")
    public ResponseEntity<ByteArrayResource> getImage(
            @PathVariable("channelId") Long channelId,
            @PathVariable("attachFileId") Long attachFileId) throws UnsupportedEncodingException {
        Pair<byte[], String> data = attachFileService.getAttachFile(channelId, attachFileId);
        ByteArrayResource resource = new ByteArrayResource(data.getLeft());
        return ResponseEntity
                .ok()
                .contentLength(data.getLeft().length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(String.valueOf(data.getRight()), "utf-8") + "\"")
                .body(resource);
    }
}