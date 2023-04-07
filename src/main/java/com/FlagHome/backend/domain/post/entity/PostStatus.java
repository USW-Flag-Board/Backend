package com.FlagHome.backend.domain.post.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum PostStatus {
    NORMAL("정상"),
    REPORTED("신고"),
    BANNED("제재"),
    DELETED("삭제");

    private String status;
}
