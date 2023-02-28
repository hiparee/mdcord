package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class ChannelNotFoundException extends AbstractException {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CHANNEL_NOT_FOUND;
    }

    public ChannelNotFoundException(Long id) {
        super("Channel not found with this ID " + id + ".");
    }
}
