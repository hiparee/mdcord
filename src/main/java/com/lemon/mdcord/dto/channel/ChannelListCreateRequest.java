package com.lemon.mdcord.dto.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ChannelListCreateRequest {

    @NotBlank
    private String name;

    private Long parentId;

    @NotBlank
    private String createBy;

}
