package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.chat.ChannelChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelChatRepository extends JpaRepository<ChannelChat, Long> {
}
