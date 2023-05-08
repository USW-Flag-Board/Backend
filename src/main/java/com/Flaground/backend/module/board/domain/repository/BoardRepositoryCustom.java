package com.Flaground.backend.module.board.domain.repository;

import com.Flaground.backend.module.board.controller.dto.response.BoardInfo;
import com.Flaground.backend.module.board.domain.BoardType;

import java.util.List;

public interface BoardRepositoryCustom {
    /**
     * Version 1
     */
//    List<Board> findAll();
//    HashSet<String> findHashSetOfBoardsName();

    /**
     * Version 2
     */
    List<BoardInfo> getBoards(BoardType boardType);
}
