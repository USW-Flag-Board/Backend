package com.Flaground.backend.module.board.domain.repository;

import com.Flaground.backend.module.board.controller.dto.response.BoardInfo;
import com.Flaground.backend.module.board.domain.BoardType;

import java.util.List;

public interface BoardRepositoryCustom {
    List<BoardInfo> getBoards(BoardType boardType);
}
