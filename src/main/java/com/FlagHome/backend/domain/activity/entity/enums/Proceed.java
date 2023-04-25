package com.FlagHome.backend.domain.activity.entity.enums;

import com.FlagHome.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum Proceed {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    BOTH("혼합")
    ;

    private final String proceed;
}