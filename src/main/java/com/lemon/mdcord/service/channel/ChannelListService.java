package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.dto.channel.ChannelListCreateRequest;
import com.lemon.mdcord.dto.channel.MultipleChannelListResponse;

public interface ChannelListService {
    ChannelList createChannel(ChannelListCreateRequest dto);

    MultipleChannelListResponse fetchChannels();
}
