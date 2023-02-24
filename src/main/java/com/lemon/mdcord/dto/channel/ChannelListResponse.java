package com.lemon.mdcord.dto.channel;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelListResponse {

    private Long id;

    private String name;

    private Integer dept;

    private Long parentId;

    private Integer channelOrder;

    private String useYn;

    @Builder
    public ChannelListResponse(Long id, String name, Integer dept, Long parentId, Integer channelOrder, String useYn) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.parentId = parentId;
        this.channelOrder = channelOrder;
        this.useYn = useYn;
    }
}
