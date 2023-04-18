package com.FlagHome.backend.infra.aws.s3.entity.enums;

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
