package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.dto.channel.list.*;

import java.util.List;
import java.util.Set;

public interface ChannelListService {
    ChannelListCreateResponse createChannel(ChannelListCreateRequest dto);

    MultipleChannelListResponse fetchChannels();

    void deleteChannel(Long id);

    ChannelListUpdateResponse updateChannelInfo(ChannelListUpdateRequest dto);

    void updateChannelOrder(List<ChannelListOrderUpdateRequest> list);

    List<ChannelList> findByParentId(Set<Long> channelIds);
}
