package com.lemon.mdcord.service.chat;

import com.lemon.mdcord.common.exception.ChannelNotFoundException;
import com.lemon.mdcord.common.exception.MemberNotFoundException;
import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.domain.chat.ChannelChat;
import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.chat.ChatCreateRequest;
import com.lemon.mdcord.repository.ChannelChatRepository;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JpaChannelChatService implements ChannelChatService {

    private final ChannelChatRepository channelChatRepository;
    private final ChannelListRepository channelListRepository;
    private final MemberRepository memberRepository;

    @Override
    public ChannelChat createChannelChat(ChatCreateRequest request) {
        String memberId = request.getMemberId();
        Long channelId = request.getChannelId();
        ChannelList channelList = channelListRepository.findById(channelId).orElseThrow(() -> new ChannelNotFoundException(channelId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        ChannelChat channelChat = ChannelChat.builder()
                .channelList(channelList)
                .member(member)
                .content(request.getContent())
                .fileYn(request.getFileYn())
                .createBy(getAuthentication().getName())
                .build();

        return channelChatRepository.save(channelChat);
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
