package com.lemon.mdcode.dto.member;

import com.lemon.mdcode.domain.member.Member;
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
