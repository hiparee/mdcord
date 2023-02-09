package com.lemon.mdcord.service.member;

import com.lemon.mdcord.common.exception.MemberNotFoundException;
import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.domain.member.MemberRole;
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

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));

        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .roles(MemberRole.USER.getValue())
                .build();
    }
}