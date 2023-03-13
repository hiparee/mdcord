package com.lemon.mdcord.service.chat;

import com.lemon.mdcord.domain.chat.AttachFile;
import com.lemon.mdcord.domain.chat.ChannelChat;
import com.lemon.mdcord.dto.chat.ChatCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChannelChatService {

    ChannelChat createChannelChat(ChatCreateRequest request);
    List<AttachFile> createAttachFile(MultipartFile[] files, Long chatId);

    ChannelChat changeChannelChatInfo(ChatCreateRequest request);
}
