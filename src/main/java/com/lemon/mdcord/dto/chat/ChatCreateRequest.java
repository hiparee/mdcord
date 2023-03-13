package com.lemon.mdcord.dto.chat;

import lombok.Getter;

@Getter
public class ChatCreateRequest {

    private Long channelId;
    private Long chatId;
    private String memberId;
    private String content;
    private String fileYn;
    private MessageType messageType;

}
