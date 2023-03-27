package com.lemon.mdcord.service.member;

import com.lemon.mdcord.common.exception.ChannelNotFoundException;
import com.lemon.mdcord.common.exception.MemberDuplicatedException;
import com.lemon.mdcord.common.exception.MemberNotFoundException;
import com.lemon.mdcord.common.security.jwt.JwtProvider;
import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.domain.channel.ChannelMember;
import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.member.*;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.repository.ChannelMemberRepository;
import com.lemon.mdcord.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@Transactional
public class JpaMemberService implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberPasswordEncoder memberPasswordEncoder;
    private final ChannelListRepository channelListRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final JwtProvider jwtProvider;
    private final String LOGIN_USE_YN = "Y";

    public JpaMemberService(MemberRepository memberRepository
            , MemberPasswordEncoder memberPasswordEncoder
            , ChannelMemberRepository channelMemberRepository
            , ChannelListRepository channelListRepository
            , JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.channelMemberRepository = channelMemberRepository;
        this.channelListRepository = channelListRepository;
        this.memberPasswordEncoder = memberPasswordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public MemberLoginResponse memberLogin(final MemberLoginRequest dto, HttpServletResponse response) {
        Member member = memberRepository.findMemberByIdAndUseYn(dto.getMemberId(), LOGIN_USE_YN).orElseThrow(() -> new MemberNotFoundException(dto.getMemberId()));
        member.checkPassword(dto.getPassword(), memberPasswordEncoder);
        this.createToken(member, response);

        return new MemberLoginResponse(member);
    }

    @Override
    public MemberCreateResponse createMember(final MemberCreateRequest dto) {
        Optional<Member> checkDuplicated = memberRepository.findById(dto.getMemberId());

        if(checkDuplicated.isPresent()) {
            throw new MemberDuplicatedException(dto.getMemberId());
        }

        String currentMemberId = getAuthentication().getName();

        Member member = Member.builder()
                .id(dto.getMemberId())
                .name(dto.getName())
                .iconFileId(getRandomIconFileId())
                .password(dto.getPassword())
                .passwordEncoder(memberPasswordEncoder)
                .createBy(currentMemberId)
                .build();

        Member savedMember = memberRepository.save(member);

        long rootChannelId = -1l;
        ChannelList rootChannelList = channelListRepository.findById(rootChannelId).orElseThrow(() -> new ChannelNotFoundException(rootChannelId));
        ChannelMember channelMember = ChannelMember.builder()
                .member(savedMember)
                .channelList(rootChannelList)
                .createBy(currentMemberId)
                .build();
        channelMemberRepository.save(channelMember);

        return new MemberCreateResponse(savedMember);
    }

    @Override
    public MemberUpdateResponse updateUser(final String memberId, final MemberUpdateRequest dto) {
        Member member = getMemberById(memberId);

        String currentMemberId = getAuthentication().getName();

        member.updateMemberInfo(
                dto.getName(), dto.getPassword(),
                memberPasswordEncoder, dto.getIconFileId(),
                dto.getRole(), dto.getUseYn(),
                currentMemberId
        );

        return new MemberUpdateResponse(member);
    }

    @Override
    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }

    @Override
    public void memberLogout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtProvider.resolveToken(request);
        jwtProvider.deleteTokenInCookie(token, response);
    }

    private static int getRandomIconFileId() {
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        int minValue = 1;
        int maxValue = 50;
        int randomIconFileId = tlr.nextInt(minValue, maxValue +1);
        return randomIconFileId;
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Member getMemberById(final String memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    private void createToken(Member member, HttpServletResponse response) {
        String token = jwtProvider.createToken(member.getId(), member.getMemberRole().getValue());
        jwtProvider.createTokenInCookie(token, response);
    }

}
