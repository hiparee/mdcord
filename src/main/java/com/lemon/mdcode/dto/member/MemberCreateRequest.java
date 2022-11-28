package com.lemon.mdcode.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    private String useYn;

    @NotBlank
    private String createBy;

}
