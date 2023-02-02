package com.FlagHome.backend.domain.sleeping.repository;

import com.FlagHome.backend.domain.sleeping.entity.Sleeping;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.domain.sleeping.entity.QSleeping.sleeping;

@Repository
@RequiredArgsConstructor
public class SleepingRepositoryImpl implements SleepingRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Sleeping> getAllSleeping() {
        final LocalDateTime limit = LocalDateTime.now().minusWeeks(2);
        return queryFactory
                .selectFrom(sleeping)
                .where()
                .fetch();
    }
}
