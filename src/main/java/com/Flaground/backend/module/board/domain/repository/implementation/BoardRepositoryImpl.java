package com.Flaground.backend.module.board.domain.repository.implementation;

import com.Flaground.backend.module.board.controller.dto.response.BoardInfo;
import com.Flaground.backend.module.board.controller.dto.response.QBoardInfo;
import com.Flaground.backend.module.board.domain.BoardType;
import com.Flaground.backend.module.board.domain.repository.BoardRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Flaground.backend.module.board.domain.QBoard.board;


@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardInfo> getBoards(BoardType boardType) {
        return queryFactory
                .select(new QBoardInfo(
                        board.id,
                        board.name))
                .from(board)
                .where(board.boardType.eq(boardType))
                .fetch();
    }
}
