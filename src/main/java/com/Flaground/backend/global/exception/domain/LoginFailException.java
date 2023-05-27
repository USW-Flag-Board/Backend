package com.Flaground.backend.global.exception.domain;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class LoginFailException extends CustomException {

    public LoginFailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
