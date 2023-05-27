package com.Flaground.backend.global.exception;

import com.Flaground.backend.global.exception.domain.CustomBadCredentialException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonPropertyOrder({"errorCode", "message"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private ErrorCode errorCode;
    private String message;
    private Integer payload; // todo: 제네릭하게 수정하거나, 다른 방법 찾기

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.payload = null;
    }

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.payload = null;
    }

    public ErrorResponse(CustomBadCredentialException e) {
        this.errorCode = e.getErrorCode();
        this.message = e.getMessage();
        this.payload = e.getFailCount();
    }
}
