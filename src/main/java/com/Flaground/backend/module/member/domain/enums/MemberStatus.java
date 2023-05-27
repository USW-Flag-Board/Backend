package com.Flaground.backend.module.member.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
    NORMAL("일반"),
    BANNED("정지"),
    LOCKED("잠금"),
    WITHDRAW("회원 탈퇴"),
    SLEEPING("휴면계정");

    private final String status;
}
