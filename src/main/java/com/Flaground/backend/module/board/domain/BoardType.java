package com.Flaground.backend.module.board.domain;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum BoardType {
    NOTICE("NOTICE"),
    MAIN("MAIN"),
    ACTIVITY("ACTIVITY");

    private final String boardName;
}
