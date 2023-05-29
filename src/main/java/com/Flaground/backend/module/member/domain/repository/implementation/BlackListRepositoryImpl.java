package com.Flaground.backend.module.member.domain.repository.implementation;

import com.Flaground.backend.module.member.domain.enums.BlackType;
import com.Flaground.backend.module.member.domain.repository.BlackListRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.Flaground.backend.module.member.domain.QBlackList.blackList;

@RequiredArgsConstructor
public class BlackListRepositoryImpl implements BlackListRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByEmail(String email) {
        return queryFactory
                .selectFrom(blackList)
                .where(blackList.email.eq(email),
                        blackList.blackType.eq(BlackType.SUSPEND))
                .fetchFirst() != null;
    }
}
