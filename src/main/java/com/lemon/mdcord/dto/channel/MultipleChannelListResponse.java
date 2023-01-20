package com.lemon.mdcord.dto.channel;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MultipleChannelListResponse {

    private List<ChannelListResponse> channelLists;

    private Integer channelCount;

    @Builder
    public MultipleChannelListResponse(List<ChannelListResponse> channelLists, Integer channelCount) {
        this.channelLists = channelLists;
        this.channelCount = channelCount;
    }


}
