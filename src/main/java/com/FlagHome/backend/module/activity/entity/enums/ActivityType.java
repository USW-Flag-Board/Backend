package com.FlagHome.backend.module.activity.entity.enums;

import com.FlagHome.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum ActivityType {
    PROJECT("프로젝트"),
    STUDY("스터디"),
    MENTORING("멘토링");

    private final String type;
}
