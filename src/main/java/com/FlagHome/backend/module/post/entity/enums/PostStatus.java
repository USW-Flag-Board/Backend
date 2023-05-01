package com.FlagHome.backend.module.post.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostStatus {
    NORMAL("정상"),
    REPORTED("신고"),
    BANNED("제재"),
    DELETED("삭제");

    private String status;
}
