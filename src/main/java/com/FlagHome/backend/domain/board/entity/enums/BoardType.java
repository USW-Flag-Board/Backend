package com.FlagHome.backend.domain.board.entity.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum BoardType {
    MAIN("메인 게시판"),
    ACTIVITY("활동 게시판");

    private String boardName;
}
