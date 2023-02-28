package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class ChannelAlreadyDisabled extends AbstractException {
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CHANNEL_ALREADY_DISABLED;
    }

    public ChannelAlreadyDisabled() {
        super("This channel is already disabled.");
    }
}
