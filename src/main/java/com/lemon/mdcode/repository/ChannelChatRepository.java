package com.lemon.mdcode.repository;

import com.lemon.mdcode.domain.chat.ChannelChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelChatRepository extends JpaRepository<ChannelChat, Long> {
}
