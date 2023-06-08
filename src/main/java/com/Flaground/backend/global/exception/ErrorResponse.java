package com.Flaground.backend.global.exception;

import com.Flaground.backend.global.exception.domain.CustomBadCredentialException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({"errorCode", "message"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<P> {
    private ErrorCode errorCode;
    private String message;
    private P payload;

    @Builder
    public ErrorResponse(ErrorCode errorCode, String message, P payload) {
        this.errorCode = errorCode;
        this.message = message;
        this.payload = payload;
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorCode.getMessage())
                .payload(null)
                .build();
    }

    public static ErrorResponse withMessage(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .payload(null)
                .build();
    }

    public static ErrorResponse loginFail(CustomBadCredentialException e) {
        return ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .message(e.getMessage())
                .payload(e.getFailCount())
                .build();
    }
}
