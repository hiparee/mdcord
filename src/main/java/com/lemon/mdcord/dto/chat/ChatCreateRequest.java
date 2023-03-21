package com.lemon.mdcord.dto.chat;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatCreateRequest {

    private Long channelId;
    private Long chatId;
    private String memberId;
    private String content;
    private String fileYn;
    private MessageType messageType;
    private List<ChatAttachFileResponse> fileList;

}
