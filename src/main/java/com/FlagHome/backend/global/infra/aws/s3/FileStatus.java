package com.FlagHome.backend.global.infra.aws.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileStatus {
    NORMAL("일반"),
    REPORTED("신고"),
    ;

    private final String status;
}
