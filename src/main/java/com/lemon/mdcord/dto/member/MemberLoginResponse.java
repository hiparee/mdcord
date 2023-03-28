package com.lemon.mdcord.dto.member;

import com.lemon.mdcord.domain.member.Member;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class MemberLoginResponse {

    @NotBlank
    private String memberId;
    @NotBlank
    private String name;
    private Integer iconFileId;
    @NotBlank
    private String useYn;
    private String role;

    public MemberLoginResponse(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.iconFileId = member.getIconFileId();
        this.useYn = member.getUseYn();
        this.role = member.getMemberRole().getValue();
    }

}
