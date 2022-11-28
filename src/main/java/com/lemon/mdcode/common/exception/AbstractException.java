package com.lemon.mdcode.common.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractException extends RuntimeException {

    private static final long serialVersionUID = -916091708945238141L;

    public abstract HttpStatus getHttpStatus();

    public abstract ErrorCode getErrorCode();

    public AbstractException() {
        super();
    }

    public AbstractException(String message) {
        super(message);
    }

    public AbstractException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbstractException(Throwable cause) {
        super(cause);
    }

}
