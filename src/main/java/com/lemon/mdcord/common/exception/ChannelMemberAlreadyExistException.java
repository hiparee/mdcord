package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class ChannelMemberAlreadyExistException extends AbstractException {

    private static final long serialVersionUID = 2303347176848153137L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.CHANNEL_MEMBER_ALREADY_EXIST;
    }

    public ChannelMemberAlreadyExistException(String memberId, Long channelId) {
        super("member " + memberId + " is already existed on channel id " + channelId + " ");
    }

}
