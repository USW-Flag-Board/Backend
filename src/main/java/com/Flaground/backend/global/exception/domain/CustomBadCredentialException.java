package com.Flaground.backend.global.exception.domain;

import com.Flaground.backend.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomBadCredentialException extends LoginFailException {
    private final int failCount;

    public CustomBadCredentialException(ErrorCode errorCode, int failCount) {
        super(errorCode);
        this.failCount = failCount;
    }
}
