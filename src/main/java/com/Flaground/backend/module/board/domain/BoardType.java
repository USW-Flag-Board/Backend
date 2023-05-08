package com.Flaground.backend.module.board.domain;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum BoardType {
    main("main"),
    activity("activity");

    private final String boardName;


    @Override
    public String toString() {
        return boardName;
    }
}
