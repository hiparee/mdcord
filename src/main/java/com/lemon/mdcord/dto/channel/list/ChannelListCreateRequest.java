package com.lemon.mdcord.dto.channel.list;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

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
    @Min(0) @Max(2)
    @Schema(description = "채널 레벨", example = "0")
    private Integer dept;

    @NotNull
    @Min(0) @Max(Integer.MAX_VALUE)
    @Schema(description = "채널 순서", example = "0")
    private Integer channelOrder;

}
