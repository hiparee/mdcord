package com.lemon.mdcord.dto.channel.member;

import com.lemon.mdcord.domain.channel.ChannelMember;
import lombok.Getter;


@Getter
public class ChannelMemberReseponse {

    private String memberId;
    private Long channelId;
    private String state;

    public ChannelMemberReseponse(ChannelMember ch) {
        this.memberId = ch.getMember().getId();
        this.channelId = ch.getChannelList().getId();
        this.state = ch.getState();
    }
}
