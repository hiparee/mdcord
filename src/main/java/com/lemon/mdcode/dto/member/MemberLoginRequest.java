package com.lemon.mdcode.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
public class MemberLoginRequest {

    @NotBlank
    private String memberId;

    @NotBlank
    private String password;

    @Builder
    public MemberLoginRequest(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

}
