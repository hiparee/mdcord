package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class InvalidChannelIdException extends AbstractException {

    private static final long serialVersionUID = -4487573283841204594L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CHANNEL_ID_INVALID;
    }

    public InvalidChannelIdException(List<Long> channelIds) {
        super("channel ID list : " + channelIds + " are invalid.");
    }

}
