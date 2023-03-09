package com.lemon.mdcord.dto.channel.member;

import com.lemon.mdcord.domain.channel.ChannelMember;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChannelMemberCreateReseponse {

    private String memberId;
    private Long channelId;
    private String createBy;
    private LocalDateTime createDate;

    public ChannelMemberCreateReseponse(ChannelMember channelMember) {
        this.memberId = channelMember.getMember().getId();
        this.channelId = channelMember.getChannelList().getId();
        this.createBy = channelMember.getCreateBy();
        this.createDate = channelMember.getCreateDate();
    }
}
