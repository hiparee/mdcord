package com.lemon.mdcord.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ChannelListCreateRequest {

    @NotBlank
    @Size(max = 50)
    @Schema(description = "채널명", example = "TEST", maxLength = 50)
    private String name;

    @NotNull
    @Schema(description = "상위채널 ID", example = "1")
    private Long parentId;

    @NotNull
    @Schema(description = "채널 레벨", example = "0")
    private Integer dept;

    @NotNull
    @Schema(description = "채널 순서", example = "0")
    private Integer channelOrder;

}
