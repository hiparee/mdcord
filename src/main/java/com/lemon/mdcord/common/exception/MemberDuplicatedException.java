package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class MemberDuplicatedException extends AbstractException{

    private static final long serialVersionUID = 7572081470779304815L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.MEMBER_DUPLICATED;
    }

    public MemberDuplicatedException(String memberId) {
        super("ID " + memberId + " is duplicated.");
    }
}
