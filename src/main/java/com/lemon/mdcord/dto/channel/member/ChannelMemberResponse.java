package com.lemon.mdcord.dto.channel.member;

import com.lemon.mdcord.domain.channel.ChannelMember;
import lombok.Getter;


@Getter
public class ChannelMemberResponse {

    private String memberId;
    private Long channelId;
    private String state;
    private String memberName;
    private Integer iconFileId;


    public ChannelMemberResponse(ChannelMember ch) {
        this.memberId = ch.getMember().getId();
        this.channelId = ch.getChannelList().getId();
        this.state = ch.getState();
        this.memberName = ch.getMember().getName();
        this.iconFileId = ch.getMember().getIconFileId();
    }
}
