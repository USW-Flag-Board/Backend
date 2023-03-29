package com.FlagHome.backend.domain.post.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum PostStatus {
    NORMAL("정상"), REPORTED("신고"), BANNED("제재");

    private String status;
}
