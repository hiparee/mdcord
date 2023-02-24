package com.lemon.mdcord.dto.channel;

import com.lemon.mdcord.domain.channel.ChannelList;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChannelListCreateResponse {

    private Long id;

    private String name;

    private Integer channelOrder;

    private LocalDateTime createDate;

    private String createBy;

    public ChannelListCreateResponse(ChannelList dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.channelOrder = dto.getChannelOrder();
        this.createDate = dto.getCreateDate();
        this.createBy = dto.getCreateBy();
    }

}
