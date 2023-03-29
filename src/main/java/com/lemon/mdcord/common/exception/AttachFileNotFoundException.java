package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class AttachFileNotFoundException extends AbstractException {

    private static final long serialVersionUID = -7760501377185150038L;


    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ATTACH_FILE_NOT_FOUND;
    }

    public AttachFileNotFoundException(String fileName) {
        super("Attach file not found with this file name " + fileName + ".");
    }
}
