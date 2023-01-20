package com.lemon.mdcord.dto.member;

import com.lemon.mdcord.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberUpdateResponse {

    private String name;

    private Integer iconFileId;

    private String useYn;

    public MemberUpdateResponse(Member member) {
        this.name = member.getName();
        this.iconFileId = member.getIconFileId();
        this.useYn = member.getUseYn();
    }

}
