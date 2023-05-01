package com.FlagHome.backend.module.member.domain.repository;

import com.FlagHome.backend.module.member.domain.Sleeping;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.module.member.domain.QSleeping.sleeping;


@Repository
@RequiredArgsConstructor
public class SleepingRepositoryImpl implements SleepingRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Sleeping> getAllSleeping() {
        final LocalDateTime limit = LocalDateTime.now().minusDays(60);
        return queryFactory
                .selectFrom(sleeping)
                .where(sleeping.expiredAt.before(limit))
                .fetch();
    }
}
