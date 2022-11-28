package com.lemon.mdcode.dto.channel;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChannelCreateRequest {

    private Integer parentId;

    @NotBlank
    private String channelName;

    private String useYn;

    private Integer dept;

    @NotBlank
    private String createBy;

}
