package com.FlagHome.backend.domain.activity.repository;


import com.FlagHome.backend.domain.activity.entity.Activity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

import static com.FlagHome.backend.domain.activity.entity.QActivity.activity;

@Repository
@Transactional
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Activity> findByActivityId(long activityId) {
        return jpaQueryFactory
                .selectFrom(activity)
                .where(activity.id.eq(activityId))
                .fetch();
    }
}
