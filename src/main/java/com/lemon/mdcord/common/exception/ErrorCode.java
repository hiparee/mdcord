package com.lemon.mdcord.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND("해당 유저를 찾을 수 없습니다.", HttpStatus.NO_CONTENT.value()),
    MEMBER_DUPLICATED("이미 등록된 유저입니다.", HttpStatus.CONFLICT.value()),
    PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    CHANNEL_LIST_DUPLICATED("이미 등록된 채널입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_VALUE("부정확한 값이 입력되었습니다.", HttpStatus.BAD_REQUEST.value()),
    INTERNAL_ERROR("INTERNAL ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value());

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
