package com.Flaground.backend.module.board.controller.dto.response;

import com.Flaground.backend.module.board.domain.BoardType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponse {
    private List<BoardInfo> boards;
    private BoardType type;

    @Builder
    public BoardResponse(List<BoardInfo> boards, BoardType type) {
        this.boards = boards;
        this.type = type;
    }

    public static BoardResponse of(List<BoardInfo> boards, BoardType boardType) {
        return BoardResponse.builder()
                .boards(boards)
                .type(boardType)
                .build();
    }
}
