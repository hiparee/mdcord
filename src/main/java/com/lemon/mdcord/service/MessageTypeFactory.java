package com.lemon.mdcord.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class MessageTypeFactory {
    private final Set<MessageTypeInterface> messageTypeInterfaces;

    public MessageTypeInterface getMessageTypeInterface(String messageType) {
        return messageTypeInterfaces.stream()
                .filter(messageTypeInterface -> messageTypeInterface.support(messageType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(messageType));
    }
}
