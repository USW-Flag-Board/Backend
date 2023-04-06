package com.FlagHome.backend.domain.reply.entity;

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
