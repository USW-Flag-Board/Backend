package com.FlagHome.backend.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    /** 일반 유저, 관리자 */
    USER, ADMIN
}