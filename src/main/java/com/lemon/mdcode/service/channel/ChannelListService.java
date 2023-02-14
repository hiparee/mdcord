package com.lemon.mdcode.service.channel;

import com.lemon.mdcode.domain.channel.ChannelList;
import com.lemon.mdcode.dto.channel.ChannelListCreateRequest;
import com.lemon.mdcode.dto.channel.MultipleChannelListResponse;

import java.util.List;

public interface ChannelListService {
    ChannelList createChannelList(ChannelListCreateRequest dto);

    MultipleChannelListResponse fetchChannelList();
}
