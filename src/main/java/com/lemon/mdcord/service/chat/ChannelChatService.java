package com.lemon.mdcord.service.chat;

import com.lemon.mdcord.domain.chat.AttachFile;
import com.lemon.mdcord.domain.chat.ChannelChat;
import com.lemon.mdcord.dto.chat.ChannelChatListResponse;
import com.lemon.mdcord.dto.chat.ChatCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChannelChatService {

    ChannelChat createChannelChat(ChatCreateRequest request);
    List<AttachFile> createAttachFile(MultipartFile[] files, Long chatId);

    ChannelChat changeChannelChatInfo(ChatCreateRequest request);

    Page<ChannelChatListResponse> getChannelChatList(Long channelId, Pageable pageable);

    ChannelChat deleteChannelChatInfo(ChatCreateRequest request);
}
