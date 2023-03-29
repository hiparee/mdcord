package com.lemon.mdcord.dto.chat;

import com.lemon.mdcord.domain.chat.AttachFile;
import com.lemon.mdcord.domain.chat.ChannelChat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ChannelChatListResponse {

    private Long chatId;
    private Long channelId;
    private String channelName;
    private String memberId;
    private String memberName;
    private Integer memberIconId;
    private String content;
    private String fileYn;
    private String fixYn;
    private Long replyChatId;
    private List<ChatAttachFileResponse> attachFileList = new ArrayList<>();
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private String deleteYn;
    private String deleteBy;

    public ChannelChatListResponse(ChannelChat cc) {
        this.chatId = cc.getId();
        this.channelId = cc.getChannelList().getId();
        this.channelName = cc.getChannelList().getName();
        this.memberId = cc.getMember().getId();
        this.memberName = cc.getMember().getName();
        this.memberIconId = cc.getMember().getIconFileId();
        this.content = cc.getContent();
        this.fileYn = cc.getFileYn();
        this.fixYn = cc.getFixYn();
        this.replyChatId = cc.getReplyChatId();
        if(cc.getAttachFiles().size() > 0) {
            for(AttachFile af : cc.getAttachFiles()) {
                this.attachFileList.add(new ChatAttachFileResponse(af));
            }
        }
        this.createDate = cc.getCreateDate();
        this.createBy = cc.getCreateBy();
        this.updateDate = cc.getUpdateDate();
        this.updateBy = cc.getUpdateBy();
        this.deleteYn = cc.getDeleteYn();
        this.deleteBy = cc.getDeleteBy();
    }
}
