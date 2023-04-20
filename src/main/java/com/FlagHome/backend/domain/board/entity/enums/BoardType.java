package com.FlagHome.backend.domain.board.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    MAIN("메인 게시판"),
    ACTIVITY("활동 게시판");

    private final String boardName;
}
