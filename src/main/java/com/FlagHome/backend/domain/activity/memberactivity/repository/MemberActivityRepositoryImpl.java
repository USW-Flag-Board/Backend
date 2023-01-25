package com.FlagHome.backend.domain.activity.memberactivity.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.FlagHome.backend.domain.activity.memberactivity.entity.QMemberActivity.memberActivity;

@Repository
@RequiredArgsConstructor
public class MemberActivityRepositoryImpl implements MemberActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByActivityId(long activityId) {
        queryFactory.delete(memberActivity)
                .where(memberActivity.activity.id.eq(activityId))
                .execute();
    }
}
