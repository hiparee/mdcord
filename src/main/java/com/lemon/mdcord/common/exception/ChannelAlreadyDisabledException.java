package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class ChannelAlreadyDisabledException extends AbstractException {

    private static final long serialVersionUID = 5426492284824228270L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CHANNEL_ALREADY_DISABLED;
    }

    public ChannelAlreadyDisabledException() {
        super("This channel is already disabled.");
    }
}
