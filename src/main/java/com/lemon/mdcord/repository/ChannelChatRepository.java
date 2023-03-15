package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.domain.chat.ChannelChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelChatRepository extends JpaRepository<ChannelChat, Long> {
    Page<ChannelChat> findByChannelList(ChannelList channelId, Pageable pageable);

    Page<ChannelChat> findByChannelListAndIdLessThan(ChannelList channelId, Long chatId, Pageable pageable);
}
