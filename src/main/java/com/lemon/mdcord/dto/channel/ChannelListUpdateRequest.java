package com.lemon.mdcord.dto.channel;

import lombok.Getter;

@Getter
public class ChannelListUpdateRequest {

    private Long id;
    private String useYn;
    private String channelName;
    private Integer channelOrder;

}
