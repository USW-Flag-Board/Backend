package com.FlagHome.backend.domain.activity.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookUsage {
    USE("사용"),
    NOT_USE("미사용");

    private final String usage;
}