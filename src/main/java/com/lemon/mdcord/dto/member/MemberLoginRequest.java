package com.lemon.mdcord.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor
public class MemberLoginRequest {

    @NotBlank
    @Size(max = 255)
    @Schema(description = "사용자 ID", example = "lemon", maxLength = 255)
    private String memberId;

    @NotBlank
    @Size(min = 8, max = 20)
    @Schema(description = "비밀번호", example = "password1234", minLength = 8, maxLength = 20)
    private String password;

}
