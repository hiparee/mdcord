package com.lemon.mdcord.dto.socket;

import lombok.Getter;

@Getter
public class ChannelCreateRequest {

    private Long channelId;
    private Long serverId;
    private Integer channelDept;
    private MessageType messageType;

}
