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
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Service
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
    @Transactional
    public Member memberLogin(final MemberLoginRequest dto, HttpServletResponse response) {
        Member member = memberRepository.findMemberByIdAndUseYn(dto.getMemberId(), LOGIN_USE_YN).orElseThrow(() -> new MemberNotFoundException(dto.getMemberId()));
        member.checkPassword(dto.getPassword(), memberPasswordEncoder);
        this.createToken(member, response);

        return member;
    }

    @Override
    @Transactional
    public Member createMember(final MemberCreateRequest dto) {
        Optional<Member> checkDuplicated = memberRepository.findMemberById(dto.getMemberId());

        if(checkDuplicated.isPresent()) {
            throw new MemberDuplicatedException(dto.getMemberId());
        }

        String currentMemberId = getAuthentication().getName();

        Member member = Member.builder()
                .id(dto.getMemberId())
                .name(dto.getName())
                .password(dto.getPassword())
                .passwordEncoder(memberPasswordEncoder)
                .createBy(currentMemberId)
                .build();

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updateUser(final MemberUpdateRequest dto) {
        Member member = getMemberById(dto.getMemberId());

        String currentMemberId = getAuthentication().getName();

        member.updateMemberInfo(
                dto.getName(), dto.getPassword(),
                memberPasswordEncoder, dto.getIconFileId(),
                dto.getUseYn(), currentMemberId
        );

        return memberRepository.save(member);
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Member getMemberById(final String memberId) {
        return memberRepository.findMemberById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    }

    private void createToken(Member member, HttpServletResponse response) {
        String token = jwtProvider.createToken(member.getId(), member.getMemberRole().getValue());
        jwtProvider.createTokenInCookie(token, response);
    }

    private void saveTokenInCookie(String token, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(header, token)
                .maxAge(validitySeconds)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
    }

}
