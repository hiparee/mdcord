package com.lemon.mdcord.service.chat;

import com.lemon.mdcord.domain.chat.ChannelChat;
import com.lemon.mdcord.dto.chat.ChatCreateRequest;

public interface ChannelChatService {

    ChannelChat createChannelChat(ChatCreateRequest request);

}
