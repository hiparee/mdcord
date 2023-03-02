package com.lemon.mdcord.common.utils;

import com.lemon.mdcord.common.exception.AbstractException;
import com.lemon.mdcord.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * custom exception - response 응답 일원화
     */
    @ExceptionHandler(AbstractException.class)
    private ResponseEntity<ErrorResponse> handleControllerException(AbstractException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(
                        new ErrorResponse(
                                e.getErrorCode().getStatus(),
                                e.getErrorCode().getMessage(),
                                e.getMessage()
                        )
                );
    }

    /**
     * controller dto @Valid에 대한 exception - response 응답 일원화
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        new ErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                                HttpStatus.BAD_REQUEST.getReasonPhrase()
                        )
                );
    }

    /**
     * 이외 exception - response 응답 일원화
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Error : {} \n " +
                  "HttpStatus : {} {}\n" +
                  "Message : {}"
                  , e, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                e.getLocalizedMessage()
                        )
                );
    }

}
