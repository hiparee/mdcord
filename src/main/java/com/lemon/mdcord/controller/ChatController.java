package com.lemon.mdcord.controller;

import com.lemon.mdcord.domain.chat.AttachFile;
import com.lemon.mdcord.service.chat.ChannelChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Tag(name = "Chat Controller")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChannelChatService chatService;

    // 채팅 입력 (텍스트)

    // 채널별 채팅 목록 (50개?)

    // 채팅 수정

    // 채팅 삭제(delete_yn : Y->N)

    // 채팅 상단 고정

    @Operation(summary = "채팅 파일 등록", description = "채팅 파일 등록 API")
    @PostMapping("/chat/file-upload")
    public List<AttachFile> uploadFile(
            @RequestPart(value = "files") MultipartFile[] files,
            @RequestParam(value = "chatId") Long chatId) {
        return chatService.createAttachFile(files, chatId);
    }

}
