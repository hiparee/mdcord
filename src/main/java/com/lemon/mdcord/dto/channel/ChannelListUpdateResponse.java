package com.lemon.mdcord.dto.channel;

import com.lemon.mdcord.domain.channel.ChannelList;

public class ChannelListUpdateResponse {

    private Long id;
    private String useYn;
    private String channelName;
    private Integer channelOrder;

    public ChannelListUpdateResponse(ChannelList cl) {
        this.id = cl.getId();
        this.useYn = cl.getUseYn();
        this.channelName = cl.getName();
        this.channelOrder = cl.getChannelOrder();
    }
}
