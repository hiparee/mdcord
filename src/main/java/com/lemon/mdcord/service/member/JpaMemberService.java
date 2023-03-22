package com.lemon.mdcord.service.member;

import com.lemon.mdcord.common.exception.MemberDuplicatedException;
import com.lemon.mdcord.common.exception.MemberNotFoundException;
import com.lemon.mdcord.common.security.jwt.JwtProvider;
import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.member.MemberCreateRequest;
import com.lemon.mdcord.dto.member.MemberLoginRequest;
import com.lemon.mdcord.dto.member.MemberPasswordEncoder;
import com.lemon.mdcord.dto.member.MemberUpdateRequest;
import com.lemon.mdcord.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final JwtProvider jwtProvider;
    private final String header;
    private final long validitySeconds;
    private final String LOGIN_USE_YN = "Y";

    public JpaMemberService(MemberRepository memberRepository
            , MemberPasswordEncoder memberPasswordEncoder
            , JwtProvider jwtProvider
            , @Value("${jwt.header}") String header
            , @Value("${jwt.validity-in-seconds}") long validitySeconds) {
        this.memberRepository = memberRepository;
        this.memberPasswordEncoder = memberPasswordEncoder;
        this.jwtProvider = jwtProvider;
        this.header = header;
        this.validitySeconds = validitySeconds;
    }

    @Override
    public Member memberLogin(final MemberLoginRequest dto, HttpServletResponse response) {
        Member member = memberRepository.findMemberByIdAndUseYn(dto.getMemberId(), LOGIN_USE_YN).orElseThrow(() -> new MemberNotFoundException(dto.getMemberId()));
        member.checkPassword(dto.getPassword(), memberPasswordEncoder);
        this.createToken(member, response);

        return member;
    }

    @Override
    public Member createMember(final MemberCreateRequest dto) {
        Optional<Member> checkDuplicated = memberRepository.findById(dto.getMemberId());

        if(checkDuplicated.isPresent()) {
            throw new MemberDuplicatedException(dto.getMemberId());
        }

        String currentMemberId = getAuthentication().getName();

        int randomIconFileId = getRandomIconFileId();

        Member member = Member.builder()
                .id(dto.getMemberId())
                .name(dto.getName())
                .iconFileId(randomIconFileId)
                .password(dto.getPassword())
                .passwordEncoder(memberPasswordEncoder)
                .createBy(currentMemberId)
                .build();

        return memberRepository.save(member);
    }

    @Override
    public Member updateUser(final MemberUpdateRequest dto) {
        Member member = getMemberById(dto.getMemberId());

        String currentMemberId = getAuthentication().getName();

        member.updateMemberInfo(
                dto.getName(), dto.getPassword(),
                memberPasswordEncoder, dto.getIconFileId(),
                dto.getRole(), dto.getUseYn(),
                currentMemberId
        );

        return member;
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
