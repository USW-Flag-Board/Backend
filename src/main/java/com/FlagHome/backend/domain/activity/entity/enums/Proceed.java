package com.FlagHome.backend.domain.activity.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Proceed {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    BOTH("온/오프라인")
    ;

    private final String proceed;
}