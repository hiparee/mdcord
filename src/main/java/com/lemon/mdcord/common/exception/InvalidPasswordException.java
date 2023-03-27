package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends AbstractException {

    private static final long serialVersionUID = -2574759970411384534L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.PASSWORD_INVALID;
    }

    public InvalidPasswordException() {
        super("password");
    }
}
