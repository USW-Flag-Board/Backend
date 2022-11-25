package com.FlagHome.backend.v1.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    /** 일반 유저, 관리자 */
    USER("USER", "사용자"),
    ADMIN("ADMIN", "관리자");

    private final String key;
    private final String value;
}