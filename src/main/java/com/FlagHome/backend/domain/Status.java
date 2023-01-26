package com.FlagHome.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    // Activity, Login log
    ON("활동 중"), OFF("활동 끝"),
    // Post, Reply, File
    NORMAL("정상"), REPORTED("신고"), BANNED("제재"),
    // User
    GENERAL("일반"), SUSPENDED("활동 정지"), WATCHING("감시"), SLEEPING("휴면계정")
    ;

    private String status;
}
