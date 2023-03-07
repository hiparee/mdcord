package com.lemon.mdcord.dto.member;

import com.lemon.mdcord.domain.member.Member;
import com.lemon.mdcord.domain.member.MemberRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberListResponse {

    private String memberId;
    private String name;
    private Integer iconFileId;
    private String useYn;
    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime updateDate;
    private String updateBy;
    private MemberRole type;

    public MemberListResponse(Member member) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.iconFileId = member.getIconFileId();
        this.useYn = member.getUseYn();
        this.createDate = member.getCreateDate();
        this.createBy = member.getCreateBy();
        this.updateDate = member.getUpdateDate();
        this.updateBy = member.getUpdateBy();
        this.type = member.getMemberRole();
    }
}
