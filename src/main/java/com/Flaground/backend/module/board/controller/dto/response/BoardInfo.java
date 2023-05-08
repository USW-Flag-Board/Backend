package com.Flaground.backend.module.board.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardInfo {
    private Long id;
    private String boardName;

    @QueryProjection
    public BoardInfo(Long id, String boardName) {
        this.id = id;
        this.boardName = boardName;
    }
}
