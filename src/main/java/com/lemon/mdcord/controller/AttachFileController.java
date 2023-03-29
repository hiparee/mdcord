package com.lemon.mdcord.controller;

import com.lemon.mdcord.service.chat.AttachFileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttachFileController {

    private final AttachFileService attachFileService;

    @Operation(summary = "이미지 파일 정보 조회", description = "이미지 파일 정보 조회 API")
    @GetMapping(value = "/channels/{channelId}/attach-file/{imageName}")
    public byte[] getImage(
            @PathVariable("channelId") Long channelId,
            @PathVariable("imageName") String fileName) {
        return attachFileService.getImageFileInfo(channelId, fileName);
    }
}