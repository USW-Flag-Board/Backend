package com.FlagHome.backend.domain.board.controller.dto;

import com.FlagHome.backend.domain.board.entity.enums.BoardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequest {
    @Schema(name = "게시판 이름")
    @NotNull @NotEmpty
    private String name;

    @Schema(name = "게시판 타입")
    @NotNull @NotEmpty
    private BoardType boardType;

    @Builder
    public BoardRequest(String name, BoardType boardType) {
        this.name = name;
        this.boardType = boardType;
    }
}
