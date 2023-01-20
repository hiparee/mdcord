package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class PasswordNotMachedException extends AbstractException {

    private static final long serialVersionUID = -3215601247391230387L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.PASSWORD_NOT_MATCHED;
    }

    public PasswordNotMachedException(String memberId) {
        super("Member " + memberId + " password not matched.");
    }
}
