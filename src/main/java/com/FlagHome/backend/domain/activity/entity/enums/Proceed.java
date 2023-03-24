package com.FlagHome.backend.domain.activity.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Proceed {
    ONLINE("온라인"),
    OFFLINE("오프라인")
    ;

    private final String proceed;
}