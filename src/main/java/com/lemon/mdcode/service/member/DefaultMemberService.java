package com.lemon.mdcode.service.member;

import com.lemon.mdcode.common.exception.MemberDuplicatedException;
import com.lemon.mdcode.common.exception.MemberNotFoundException;
import com.lemon.mdcode.domain.member.Member;
import com.lemon.mdcode.dto.member.MemberCreateRequest;
import com.lemon.mdcode.dto.member.MemberLoginRequest;
import com.lemon.mdcode.dto.member.MemberPasswordEncoder;
import com.lemon.mdcode.dto.member.MemberUpdateRequest;
import com.lemon.mdcode.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {
    private final MemberRepository memberRepository;

    private final MemberPasswordEncoder memberPasswordEncoder;

    private final String LOGIN_USE_YN = "Y";

    @Override
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
                .createDate(LocalDateTime.now())
                .build();

        return memberRepository.save(member);
    }

    @Override
    public Member memberLogin(final MemberLoginRequest dto) {
        Member member = memberRepository.findMemberByIdAndUseYn(dto.getMemberId(), LOGIN_USE_YN).orElseThrow(() -> new MemberNotFoundException(dto.getMemberId()));

        member.checkPassword(dto.getPassword(), memberPasswordEncoder);

        return member;
    }

    @Override
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
