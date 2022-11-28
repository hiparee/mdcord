package com.lemon.mdcode.common.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends AbstractException {

    private static final long serialVersionUID = -5148646781865844857L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.MEMBER_NOT_FOUND;
    }

    public MemberNotFoundException(String memberId) {
        super("ID " + (memberId == null ? "" : memberId) + " dose not found.");
    }
}
