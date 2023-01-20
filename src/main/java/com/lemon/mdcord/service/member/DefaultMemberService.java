package com.lemon.mdcord.service.member;

import com.lemon.mdcord.common.exception.MemberDuplicatedException;
import com.lemon.mdcord.common.exception.MemberNotFoundException;
import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.dto.member.MemberCreateRequest;
import com.lemon.mdcord.dto.member.MemberLoginRequest;
import com.lemon.mdcord.dto.member.MemberPasswordEncoder;
import com.lemon.mdcord.dto.member.MemberUpdateRequest;
import com.lemon.mdcord.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {
    private final MemberRepository memberRepository;

    private final MemberPasswordEncoder memberPasswordEncoder;

    private final String LOGIN_USE_YN = "Y";


    @Override
    @Transactional
    public Member memberLogin(final MemberLoginRequest dto) {
        Member member = memberRepository.findMemberByIdAndUseYn(dto.getMemberId(), LOGIN_USE_YN).orElseThrow(() -> new MemberNotFoundException(dto.getMemberId()));

        member.checkPassword(dto.getPassword(), memberPasswordEncoder);

        return member;
    }

    @Override
    @Transactional
    public Member createMember(final MemberCreateRequest dto) {
        Optional<Member> checkDuplicated = memberRepository.findMemberById(dto.getMemberId());

        if(checkDuplicated.isPresent()) {
            throw new MemberDuplicatedException(dto.getMemberId());
        }

        Member member = Member.builder()
                .id(dto.getMemberId())
                .name(dto.getName())
                .password(dto.getPassword())
                .passwordEncoder(memberPasswordEncoder)
                .createBy(dto.getCreateBy())
                .build();

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updateUser(final MemberUpdateRequest dto) {
        Member member = getMemberById(dto.getMemberId());

        member.updateMemberInfo(
                dto.getName(), dto.getPassword(),
                memberPasswordEncoder, dto.getIconFileId(),
                dto.getUseYn(), dto.getUpdateBy()
        );

        return memberRepository.save(member);
    }

    private Member getMemberById(final String memberId) {
        return memberRepository.findMemberById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
    }

}
