package com.lemon.mdcord.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {

    @NotBlank
    @Size(max = 20)
    @Schema(description = "사용자 ID", example = "lemon", maxLength = 20)
    private String memberId;

    @NotBlank
    @Size(max = 10)
    @Schema(description = "사용자 이름", example = "사용자", maxLength = 10)
    private String name;

    @NotBlank
    @Size(min = 8, max = 20)
    @Schema(description = "비밀번호", example = "password1234", minLength = 8, maxLength = 20)
    private String password;

    @NotNull
    @Schema(description = "가입 채널", example = "1")
    private Long channelId;

}
