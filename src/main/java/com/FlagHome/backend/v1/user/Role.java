package com.FlagHome.backend.v1.user;

import lombok.Getter;

@Getter
public enum Role {
    /**
     * 일반 유저, 관리자?
     */
    ADMIN("ROLE_ADMIN"),
    NORMAL("ROLE_NORMAL");

    Role(String value) {
        this.value = value;
    }

    private final String value;
}
