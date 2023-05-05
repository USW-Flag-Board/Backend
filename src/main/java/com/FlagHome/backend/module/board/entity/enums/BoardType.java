package com.FlagHome.backend.module.board.entity.enums;

import com.FlagHome.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum BoardType {
    MAIN("메인 게시판"),
    ACTIVITY("활동 게시판");

    private final String boardName;
}
