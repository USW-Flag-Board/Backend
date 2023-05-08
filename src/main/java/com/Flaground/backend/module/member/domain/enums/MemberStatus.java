package com.Flaground.backend.module.member.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberStatus {
    NORMAL("일반"),
    BANNED("정지"),
    WATCHING("감시"),
    WITHDRAW("회원 탈퇴"),
    SLEEPING("휴면계정");

    private final String status;
}
