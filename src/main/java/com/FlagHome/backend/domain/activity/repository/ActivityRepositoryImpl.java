package com.FlagHome.backend.domain.activity.repository;

import com.FlagHome.backend.domain.activity.controller.dto.response.ActivityResponse;
import com.FlagHome.backend.domain.activity.controller.dto.response.QActivityResponse;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.FlagHome.backend.domain.activity.entity.QActivity.activity;
import static com.FlagHome.backend.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ActivityResponse> getAllActivities() {
        return queryFactory
                .select(new QActivityResponse(
                        activity.id,
                        activity.name,
                        member.name,
                        activity.activityType,
                        activity.status,
                        activity.semester))
                .from(activity)
                .innerJoin(activity.leader, member)
                .orderBy(activity.id.desc())
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
                        activity.semester))
                .from(activity)
                .innerJoin(activity.leader, member)
                .where(activity.status.eq(ActivityStatus.RECRUIT))
                .fetch();
    }
}
