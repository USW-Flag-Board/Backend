package com.Flaground.backend.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.Flaground.backend.global.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * CustomException으로 걸러지는 예외 핸들러
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException : {}, {}", e.getErrorCode(), e.getErrorCode().getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    /**
     * 로그인 실패로 던져지는 예외 핸들러 (비활성, 잠금 예외는 제외)
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("LoginFailed (BadCredentials) : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(PASSWORD_NOT_MATCH, PASSWORD_NOT_MATCH.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * 예상치 못한 예외를 처리하는 핸들러
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        log.error("UnexpectedException : {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    /**
     * @Valid에서 발생하는 예외를 처리하는 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        final String caused = e.getParameter().getExecutable().toGenericString();
        log.error("ValidateFailed : {}", caused);
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(errorResponse);
    }

//    /**
//     * 요청 경로는 있으나 지원하지 않는 Method인 경우 발생하는 예외
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
//                                                                         HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
//    }
//
//    /**
//     * 요청의 Content Type을 핸들러가 지원하지 않는 경우 발생하는 예외
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
//                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
//    }
//
//    /**
//     * 핸들러가 Client가 요청한 Type으로 응답을 내려줄 수 없는 경우 발생하는 예외
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
//                                                                      HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
//    }
//
//    /**
//     * 핸들러가 URL에서 기대한 Path Variable을 찾지 못한 경우 발생하는 예외
//     */
//    @Override
//    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
//                                                               HttpStatus status, WebRequest request) {
//        return super.handleMissingPathVariable(ex, headers, status, request);
//    }
//
//    /**
//     * 핸들러가 기대한 요청 Parameter를 찾지 못한 경우 발생하는 예외
//     */
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
//                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleMissingServletRequestParameter(ex, headers, status, request);
//    }
}
