package com.lemon.mdcode.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberUpdateRequest {

    private String memberId;

    private String name;

    private String password;

    private Integer iconFileId;

    private String useYn;

    @NotBlank
    private String updateBy;

}
