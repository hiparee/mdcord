package com.lemon.mdcord.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ChannelListCreateRequest {

    @NotBlank
    @Size(max = 50)
    @Schema(description = "채널명", example = "미션개발팀", maxLength = 50)
    private String name;

    @Schema(description = "상위채널 ID", example = "1")
    private Long parentId;

}
