package com.FlagHome.backend.global.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @ApiModelProperty(example = "LOGIN_FAILED")
    private ErrorCode errorCode;

    @ApiModelProperty(example = "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.")
    private String message;
}
