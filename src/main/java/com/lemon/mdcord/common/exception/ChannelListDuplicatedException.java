package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class ChannelListDuplicatedException extends AbstractException{

    private static final long serialVersionUID = 3253432537836440678L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CHANNEL_LIST_DUPLICATED;
    }

    public ChannelListDuplicatedException(String channelListId) {
        super("channel name " + channelListId + " is duplicated.");
    }
}
