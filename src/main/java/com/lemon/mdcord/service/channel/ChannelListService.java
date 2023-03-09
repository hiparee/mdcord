package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.dto.channel.list.ChannelListCreateRequest;
import com.lemon.mdcord.dto.channel.list.ChannelListUpdateRequest;
import com.lemon.mdcord.dto.channel.list.MultipleChannelListResponse;

public interface ChannelListService {
    ChannelList createChannel(ChannelListCreateRequest dto);

    MultipleChannelListResponse fetchChannels();

    void deleteChannel(Long id);

    ChannelList updateChannel(ChannelListUpdateRequest dto);
}
