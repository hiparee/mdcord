package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.domain.channel.ChannelMember;
import com.lemon.mdcord.dto.channel.member.ChannelMemberCreateRequest;

import java.util.List;

public interface ChannelMemberService {

    ChannelMember createChannelMember(ChannelMemberCreateRequest request);
    List<ChannelMember> findByMemberId(String memberId);

}
