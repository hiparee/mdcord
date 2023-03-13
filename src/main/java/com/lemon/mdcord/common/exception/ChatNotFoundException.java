package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class ChatNotFoundException extends AbstractException {

    private static final long serialVersionUID = -8901899120721424862L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CHAT_NOT_FOUND;
    }

    public ChatNotFoundException(Long id) {
        super("Chat not found with this ID " + id + ".");
    }
}
