package com.lemon.mdcord.service.member;

import com.lemon.mdcord.common.exception.MemberNotFoundException;
import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // TODO - 기본 생성 generated security password 막을려고 만든 건데 필요가 있을지? 있다면 나중에 유저권한 테이블 생성 후 아래 builder의 roles 값 수정 필요
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .roles("USER")
                .build();
    }
}
