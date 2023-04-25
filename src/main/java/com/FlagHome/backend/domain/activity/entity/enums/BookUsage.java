package com.FlagHome.backend.domain.activity.entity.enums;

import com.FlagHome.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum BookUsage {
    USE("사용"),
    NOT_USE("미사용");

    private final String usage;
}
