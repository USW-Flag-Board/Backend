package com.FlagHome.backend.domain.activity.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityStatus {
    ON("진행 중"),
    OFF("진행 완료"),
    RECRUIT("모집 중");

    private final String status;
}
