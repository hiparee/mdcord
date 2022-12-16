package com.lemon.mdcode.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    MEMBER_NOT_FOUND("해당 유저를 찾을 수 없습니다.", 404),
    MEMBER_DUPLICATED("이미 등록된 유저입니다.", 400),
    
    PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다.", 400),

    CHANNEL_LIST_DUPLICATED("이미 등록된 채널입니다.", 400),
    
    INVALID_VALUE("부정확한 값이 입력되었습니다.", 400),

    INTERNAL_ERROR("INTERNAL ERROR", 500);

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
