package com.lemon.mdcord.dto.member;

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

    @NotBlank
    private String createBy;

}
