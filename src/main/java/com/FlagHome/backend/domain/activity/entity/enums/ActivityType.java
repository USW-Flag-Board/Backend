package com.FlagHome.backend.domain.activity.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
    PROJECT("프로젝트"),
    STUDY("스터디"),
    MENTORING("멘토링");

    private final String type;
}
