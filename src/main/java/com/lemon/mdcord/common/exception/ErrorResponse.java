package com.lemon.mdcord.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private int httpStatus;

    private String errorMessage;

    private String details;

    public ErrorResponse(int httpStatus, String errorMessage, String details) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.details = details;
    }
}
