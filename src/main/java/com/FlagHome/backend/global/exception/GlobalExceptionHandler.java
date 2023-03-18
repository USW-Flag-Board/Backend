package com.FlagHome.backend.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * CustomException으로 걸러지는 예외 핸들러
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("handleCustomException throw Exception : {}", e.getErrorCode());
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    /**
     * 로그인 실패로 던져지는 예외 핸들러
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("handleBadCredentialsException throw Exception : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.PASSWORD_NOT_MATCH, ErrorCode.PASSWORD_NOT_MATCH.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 예상치 못한 예외를 처리하는 핸들러
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(RuntimeException e) {
        log.error("handleUnexpectedException throw Exception : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
