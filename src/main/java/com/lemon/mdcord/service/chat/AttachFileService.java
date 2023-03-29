package com.lemon.mdcord.service.chat;

import org.apache.commons.lang3.tuple.Pair;

public interface AttachFileService {

    byte[] getImageFileInfo(Long channelId, String fileName);
    Pair<byte[], String> getAttachFile(Long channelId, Long attachFileId);

}
