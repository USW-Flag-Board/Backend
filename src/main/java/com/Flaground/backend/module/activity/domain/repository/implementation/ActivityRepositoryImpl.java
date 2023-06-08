package com.Flaground.backend.module.activity.domain.repository.implementation;

import com.Flaground.backend.global.common.response.SearchResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityDetailResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityResponse;
import com.Flaground.backend.module.activity.controller.dto.response.QActivityDetailResponse;
import com.Flaground.backend.module.activity.controller.dto.response.QActivityResponse;
import com.Flaground.backend.module.activity.domain.enums.ActivityStatus;
import com.Flaground.backend.module.activity.domain.repository.ActivityRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Flaground.backend.module.activity.domain.QActivity.activity;
import static com.Flaground.backend.module.member.domain.QMember.member;
import static com.querydsl.core.types.dsl.Expressions.asNumber;

@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public ActivityDetailResponse getActivityDetail(Long activityId) {
        return queryFactory
                .select(new QActivityDetailResponse(
                        asNumber(activityId),
                        activity.name,
                        activity.description,
                        member.name,
                        activity.info,
                        activity.type,
                        activity.status,
                        activity.createdAt))
                .from(activity)
                .innerJoin(activity.leader, member)
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
                .limit(3)
                .fetch();
    }

    @Override
    public SearchResponse<ActivityResponse> searchActivity(String keyword) {
        List<ActivityResponse> responses = queryFactory
                .selectDistinct(new QActivityResponse(
                        activity.id,
                        activity.name,
                        member.name,
                        activity.type,
                        activity.status,
                        activity.info.semester))
                .from(activity)
                .innerJoin(activity.leader, member)
                .where(activity.name.contains(keyword)
                        .or(activity.description.contains(keyword)))
                .fetch();

        return SearchResponse.from(responses);
    }
}
