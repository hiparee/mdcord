package com.lemon.mdcord.service;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MessageTypeInterface {
    boolean support(String messageType);
    void handleModifiedMessage(String messageType, String payload, Map<Long, List<WebSocketSession>> channelMap) throws IOException;
}
