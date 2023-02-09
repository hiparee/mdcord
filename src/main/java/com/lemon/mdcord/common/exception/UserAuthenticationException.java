package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class UserAuthenticationException extends AbstractException {

    private static final long serialVersionUID = -6623929951645662302L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.UNAUTHORIZED;
    }

    public UserAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
