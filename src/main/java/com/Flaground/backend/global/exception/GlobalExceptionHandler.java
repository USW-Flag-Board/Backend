package com.Flaground.backend.global.exception;

import com.Flaground.backend.global.exception.domain.CustomBadCredentialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * CustomException으로 걸러지는 예외 핸들러
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException : {}, {}", e.getErrorCode(), e.getErrorCode().getMessage());
        ErrorResponse response = new ErrorResponse(e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

    /**
     * 로그인 실패(비밀번호 불일치)를 처리하는 예외 핸들러
     */
    @ExceptionHandler(CustomBadCredentialException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleBadCredentialsException(CustomBadCredentialException e) {
        log.error("LoginFailed (BadCredential) : {}", e.getMessage());
        return new ErrorResponse(e);
    }

    /**
     * 예상치 못한 예외를 처리하는 핸들러
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException(Exception e) {
        log.error("UnexpectedException : {}", e.getMessage());
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * @Valid에서 발생하는 예외를 처리하는 핸들러
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        final String caused = e.getParameter().getExecutable().toGenericString();
        log.error("ValidateFailed : {}", caused);
        return new ErrorResponse(ErrorCode.REQUEST_VALIDATION_FAIL, message);
    }

    /**
     * @Validated에서 발생하는 예외를 처리하는 핸들러
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        final String message = e.getMessage();
        log.error("ValidateFailed : {}", message);
        return new ErrorResponse(ErrorCode.REQUEST_VALIDATION_FAIL, message);
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(NOT_FOUND)
//    public ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
//        final String requestURI = request.getRequestURI();
//        log.error("Not Support URI, Request : {}", requestURI);
//        return new ErrorResponse(ErrorCode.URI_NOT_FOUND);
//    }

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
