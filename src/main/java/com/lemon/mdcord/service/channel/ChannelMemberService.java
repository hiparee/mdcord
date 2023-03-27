package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.domain.channel.ChannelMember;
import com.lemon.mdcord.dto.channel.member.ChannelMemberCreateRequest;
import com.lemon.mdcord.dto.channel.member.ChannelMemberCreateReseponse;
import com.lemon.mdcord.dto.channel.member.ChannelMemberResponse;

import java.util.List;

public interface ChannelMemberService {

    ChannelMemberCreateReseponse createChannelMember(ChannelMemberCreateRequest request);
    List<ChannelMember> findByMemberId(String memberId);
    List<ChannelMemberResponse> getJoinedChannelsMemberList();
    void changeAllStateOFF();
    void changeMemberState(String memberId, String state);
}
