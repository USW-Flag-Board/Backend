package com.FlagHome.backend.domain.activity.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookUsage {
    USE("사용"),
    NOT_USE("미사용");

    private final String usage;
}
