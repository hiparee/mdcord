package com.lemon.mdcord.dto.channel.member;

import lombok.Getter;

@Getter
public class ChannelMemberCreateRequest {

    private Long channelId;
    private String memberId;

    public ChannelMemberCreateRequest(Long channelId, String memberId) {
        this.channelId = channelId;
        this.memberId = memberId;
    }
}
