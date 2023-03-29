package com.FlagHome.backend.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * Version 1
     */
    /* @Override
    public List<Board> findAll(){
        return queryFactory
                .selectFrom(board)
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
    } */

    /**
     * Version 2
     */
}
