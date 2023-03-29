package com.lemon.mdcord.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    MEMBER_NOT_FOUND("해당 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    MEMBER_DUPLICATED("이미 등록된 유저입니다.", HttpStatus.CONFLICT.value()),
    PASSWORD_NOT_MATCHED("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    CHANNEL_LIST_DUPLICATED("이미 등록된 채널입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_VALUE("부정확한 값이 입력되었습니다.", HttpStatus.BAD_REQUEST.value()),
    INTERNAL_ERROR("INTERNAL ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value()),
    CHANNEL_NOT_FOUND("해당 채널 ID는 존재하지 않습니다.", HttpStatus.BAD_REQUEST.value()),
    CHANNEL_CANT_DELETE("하위 채널이 남아 있어, 해당 채널의 삭제가 불가능합니다.", HttpStatus.BAD_REQUEST.value()),
    CHANNEL_ALREADY_DISABLED("이미 사용 불가능한 채널입니다.", HttpStatus.BAD_REQUEST.value()),
    CHAT_NOT_FOUND("해당 채팅을 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    PASSWORD_INVALID("크기가 8에서 20 사이여야 합니다.", HttpStatus.BAD_REQUEST.value()),
    CHANNEL_MEMBER_ALREADY_EXIST("해당 채널에 유저가 이미 등록되어있습니다.", HttpStatus.BAD_REQUEST.value()),
    ATTACH_FILE_NOT_FOUND("해당 파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value()),
    ATTACH_FILE_NOT_MATCHED_IMAGE_FILE_EXT("해당 파일 확장자는 이미지 파일 확장자가 아닙니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
