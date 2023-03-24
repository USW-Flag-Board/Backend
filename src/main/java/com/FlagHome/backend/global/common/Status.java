package com.FlagHome.backend.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    NORMAL("정상"), REPORTED("신고"), BANNED("제재"),

    GENERAL("일반"), SUSPENDED("활동 정지"), WATCHING("감시"), SLEEPING("휴면계정")
    ;

    private final String status;
}
