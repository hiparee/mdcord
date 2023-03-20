package com.lemon.mdcord.dto.chat;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatAttachFileResponse {

    private Long id;
    private String originFileName;
    private String realFileName;
    private String fileExt;
    private Long fileSize;
    private String createDate;
    private String createBy;

}
