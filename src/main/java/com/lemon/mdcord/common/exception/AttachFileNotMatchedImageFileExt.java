package com.lemon.mdcord.common.exception;

import org.springframework.http.HttpStatus;

public class AttachFileNotMatchedImageFileExt extends AbstractException {

    private static final long serialVersionUID = -6117021703234376756L;

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ATTACH_FILE_NOT_MATCHED_IMAGE_FILE_EXT;
    }

    public AttachFileNotMatchedImageFileExt(String fileExt) {
        super("Attach file not matched image file ext. request file ext : " + fileExt + ".");
    }
}
