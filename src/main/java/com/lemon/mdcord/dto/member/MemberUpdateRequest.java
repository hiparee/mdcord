package com.lemon.mdcord.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberUpdateRequest {

    @Size(max = 255)
    @Schema(description = "사용자 ID", example = "lemon", maxLength = 255)
    private String memberId;

    @Size(max = 10)
    @Schema(description = "사용자 이름", example = "사용자", maxLength = 10)
    private String name;

    @Size(min = 8, max = 20)
    @Schema(description = "비밀번호", example = "password1234", minLength = 8, maxLength = 20)
    private String password;

    private Integer iconFileId;

    @Size(max = 1)
    @Schema(description = "사용 여부", example = "Y", maxLength = 1)
    private String useYn;

}
