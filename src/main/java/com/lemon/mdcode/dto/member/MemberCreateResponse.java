package com.lemon.mdcode.dto.member;

import com.lemon.mdcode.domain.member.Member;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class MemberCreateResponse {

    @NotBlank
    private String memberId;

    @NotBlank
    private String name;

    public MemberCreateResponse(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
    }
}
