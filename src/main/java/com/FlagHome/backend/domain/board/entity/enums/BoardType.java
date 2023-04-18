package com.FlagHome.backend.domain.board.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BoardType {
    MAIN("메인 게시판"),
    ACTIVITY("활동 게시판");

    private String boardName;
}
