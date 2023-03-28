package com.lemon.mdcord.dto.chat;

import com.lemon.mdcord.domain.chat.AttachFile;
import lombok.Getter;

@Getter
public class ChatAttachFileResponse {

    private Long id;
    private String originFileName;
    private String realFileName;
    private String fileExt;
    private Long fileSize;
    private String createDate;
    private String createBy;

    public ChatAttachFileResponse(AttachFile at) {
        this.id = at.getId();
        this.originFileName = at.getOriginFileName();
        this.realFileName = at.getRealFileName();
        this.fileExt = at.getFileExt();
        this.fileSize = at.getFileSize();
        this.createDate = at.getCreateDate().toString();
        this.createBy = at.getCreateBy();
    }
}
