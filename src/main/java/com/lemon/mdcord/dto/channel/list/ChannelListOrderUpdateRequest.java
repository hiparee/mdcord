package com.lemon.mdcord.dto.channel.list;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ChannelListOrderUpdateRequest {

    @NotBlank
    @Schema(description = "채널 ID", example = "16")
    private Long id;
    @NotBlank
    @Schema(description = "상위 채널 ID", example = "7")
    private Long parentId;
    @NotBlank
    @Schema(description = "채널 순서", example = "1")
    private Integer channelOrder;

}
