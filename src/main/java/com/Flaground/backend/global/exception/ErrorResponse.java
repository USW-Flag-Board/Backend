package com.Flaground.backend.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    @Schema(example = "LOGIN_FAILED")
    private ErrorCode errorCode;

    @Schema(example = "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.")
    private String message;

    @Builder
    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
