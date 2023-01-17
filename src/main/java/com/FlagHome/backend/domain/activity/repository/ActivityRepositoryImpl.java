package com.FlagHome.backend.domain.activity.repository;

import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.dto.QActivityResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.FlagHome.backend.domain.activity.entity.QActivity.activity;
import static com.FlagHome.backend.domain.member.entity.QMember.member;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public ActivityResponse getActivity(long activityId) {
        return queryFactory
                .select(new QActivityResponse(
                        activity.id,
                        activity.name,
                        member.name,
                        activity.activityType,
                        activity.status))
                .fetchJoin()
                .from(activity)
                .where(activity.id.eq(activityId))
                .fetchOne();
    }

    @Override
    public List<ActivityResponse> getAllActivities() {
        return queryFactory
                .select(new QActivityResponse(
                        activity.id,
                        activity.name,
                        member.name,
                        activity.activityType,
                        activity.status))
                .fetchJoin()
                .from(activity)
                .fetch();
    }
}
