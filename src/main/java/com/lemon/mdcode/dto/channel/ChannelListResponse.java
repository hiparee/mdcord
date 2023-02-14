package com.lemon.mdcode.dto.channel;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelListResponse {

    private Long id;

    private String name;

    private Long parentId;

    private String useYn;

    @Builder
    public ChannelListResponse(Long id, String name, Long parentId, String useYn) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.useYn = useYn;
    }
}
