package com.FlagHome.backend.domain.activity.repository;

import com.FlagHome.backend.domain.activity.controller.dto.response.ActivityResponse;
import com.FlagHome.backend.domain.activity.controller.dto.response.QActivityResponse;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.FlagHome.backend.domain.activity.entity.QActivity.activity;
import static com.FlagHome.backend.domain.member.entity.QMember.member;
import static com.querydsl.core.types.dsl.Expressions.asNumber;

@Repository
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ActivityResponse> getActivity(long activityId) {
        ActivityResponse activityResponse = queryFactory
                .select(new QActivityResponse(
                        asNumber(activityId).as(activity.id),
                        activity.name,
                        member.name,
                        activity.activityType,
                        activity.status,
                        activity.semester,
                        activity.createdAt))
                .from(activity)
                .innerJoin(activity.leader, member)
                .where(activity.id.eq(activityId))
                .fetchOne();

        return Optional.ofNullable(activityResponse);
    }

    @Override
    public List<ActivityResponse> getAllActivities() {
        return queryFactory
                .select(new QActivityResponse(
                        activity.id,
                        activity.name,
                        member.name,
                        activity.activityType,
                        activity.status,
                        activity.semester,
                        activity.createdAt))
                .from(activity)
                .innerJoin(activity.leader, member)
                .fetch();
    }

    @Override
    public List<ActivityResponse> getRecruitActivities() {
        return queryFactory
                .select(new QActivityResponse(
                        activity.id,
                        activity.name,
                        member.name,
                        activity.activityType,
                        activity.status,
                        activity.semester,
                        activity.createdAt))
                .from(activity)
                .innerJoin(activity.leader, member)
                .where(activity.status.eq(ActivityStatus.RECRUIT))
                .fetch();
    }
}
