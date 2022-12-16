package com.lemon.mdcode.dto.channel;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelListResponse {

    private String name;

    private Long parentId;

    private String useYn;

    @Builder
    public ChannelListResponse(String name, Long parentId, String useYn) {
        this.name = name;
        this.parentId = parentId;
        this.useYn = useYn;
    }
}
