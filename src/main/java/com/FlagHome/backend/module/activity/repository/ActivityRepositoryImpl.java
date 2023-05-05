package com.FlagHome.backend.module.activity.repository;

import com.FlagHome.backend.module.activity.controller.dto.response.ActivityResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.QActivityResponse;
import com.FlagHome.backend.module.activity.entity.enums.ActivityStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.FlagHome.backend.module.activity.entity.QActivity.activity;
import static com.FlagHome.backend.module.member.domain.QMember.member;

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
                        activity.type,
                        activity.status,
                        activity.info.semester))
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
                        activity.type,
                        activity.status,
                        activity.info.semester))
                .from(activity)
                .innerJoin(activity.leader, member)
                .where(activity.status.eq(ActivityStatus.RECRUIT))
                .fetch();
    }
}