package com.FlagHome.backend.domain.board.repository;

import com.FlagHome.backend.domain.board.entity.Board;

import java.util.HashSet;
import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> findAll();
    HashSet<String> findHashSetOfBoardsName();
}
