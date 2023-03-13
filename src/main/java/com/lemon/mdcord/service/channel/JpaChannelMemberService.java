package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.common.exception.ChannelNotFoundException;
import com.lemon.mdcord.common.exception.MemberNotFoundException;
import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.domain.channel.ChannelMember;
import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.channel.member.ChannelMemberCreateRequest;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.repository.ChannelMemberRepository;
import com.lemon.mdcord.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaChannelMemberService implements ChannelMemberService {

    private final ChannelMemberRepository channelMemberRepository;
    private final MemberRepository memberRepository;
    private final ChannelListRepository channelListRepository;

    @Override
    public ChannelMember createChannelMember(ChannelMemberCreateRequest request) {
        // TODO - 기존에 등록되어있는지 체크하기

        String memberId = request.getMemberId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Long channelId = request.getChannelId();
        ChannelList channelList = channelListRepository.findById(channelId).orElseThrow(() -> new ChannelNotFoundException(channelId));

        String createBy = getAuthentication().getName();

        ChannelMember channelMember = ChannelMember.builder()
                .member(member)
                .channelList(channelList)
                .createBy(createBy)
                .build();

        return channelMemberRepository.save(channelMember);
    }

    @Override
    public List<ChannelMember> findByMemberId(String memberId) {
        return channelMemberRepository.findByMemberId(memberId);
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}