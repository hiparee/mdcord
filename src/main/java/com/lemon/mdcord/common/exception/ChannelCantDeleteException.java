package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class ChannelCantDeleteException extends AbstractException {

    private static final long serialVersionUID = -1357844306092263953L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CHANNEL_CANT_DELETE;
    }

    public ChannelCantDeleteException() {
        super("Since the sub-channels remain, it is not possible to delete them.");
    }

}
