package com.lemon.mdcord.dto.chat;

import com.lemon.mdcord.domain.chat.AttachFile;
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
    private List<AttachFile> fileList;

}
