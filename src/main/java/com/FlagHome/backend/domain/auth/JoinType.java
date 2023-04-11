package com.FlagHome.backend.domain.auth;

import lombok.Getter;

@Getter
public enum JoinType {
    NORMAL("일반"),
    CREW("동아리");

    JoinType(String type) {
        this.type = type;
    }

    private String type;
}
