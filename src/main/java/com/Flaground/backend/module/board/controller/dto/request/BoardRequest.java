package com.Flaground.backend.module.board.controller.dto.request;

import com.Flaground.backend.global.annotation.EnumFormat;
import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.domain.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequest {
    @Schema(name = "게시판 이름")
    @NotBlank
    private String name;

    @Schema(name = "게시판 타입", example = "main / activity")
    @EnumFormat(enumClass = BoardType.class)
    private BoardType boardType;

    @Builder
    public BoardRequest(String name, BoardType boardType) {
        this.name = name;
        this.boardType = boardType;
    }

    public Board toEntity() {
        return Board.builder()
                .name(name)
                .boardType(boardType)
                .build();
    }
}
