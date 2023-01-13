package com.FlagHome.backend.domain.board.repository;

import com.FlagHome.backend.domain.board.entity.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;

import static com.FlagHome.backend.domain.board.entity.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> findAll(){
        return queryFactory
                .select(board)
                .from(board)
                .where(board.parent.isNull())
                .fetch();
    }

    @Override
    public HashSet<String> findHashSetOfBoardsName() {
        HashSet<String> categoryNameSet = new HashSet<>();
        List<Board> fetchResult = queryFactory.selectFrom(board).fetch();

        for(Board eachBoard : fetchResult)
            categoryNameSet.add(eachBoard.getEnglishName());

        return categoryNameSet;
    }
}
