package com.lemon.mdcord.dto.channel.member;

import lombok.Getter;

@Getter
public class ChannelMemberCreateRequest {

    private Long channelId;
    private String memberId;

}
