package com.lemon.mdcord.dto.member;

import com.lemon.mdcord.domain.member.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberUpdateRequest {

    @Size(max = 20)
    @Schema(description = "사용자 ID", example = "lemon", maxLength = 20)
    private String memberId;

    @Size(max = 10)
    @Schema(description = "사용자 이름", example = "사용자", maxLength = 10)
    private String name;

    @Schema(description = "비밀번호", example = "password1234")
    private String password;

    @Schema(description = "사용자 아이콘 ID", example = "1")
    private Integer iconFileId;

    @Size(max = 1)
    @Schema(description = "사용 여부", example = "Y", maxLength = 1)
    private String useYn;

    @Schema(description = "사용자 권한", example = "USER")
    private MemberRole role;

}
