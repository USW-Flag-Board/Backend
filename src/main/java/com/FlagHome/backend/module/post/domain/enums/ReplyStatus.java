package com.FlagHome.backend.module.post.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReplyStatus {
    NORMAL("정상"),
    REPORTED("신고"),
    BANNED("제제");

    private String status;
}
