package com.Flaground.backend.module.activity.domain.repository.implementation;

import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.Flaground.backend.module.activity.controller.dto.response.QActivityApplyResponse;
import com.Flaground.backend.module.activity.domain.repository.ActivityApplyRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Flaground.backend.module.activity.domain.QActivityApply.activityApply;
import static com.Flaground.backend.module.member.domain.QMember.member;

@RequiredArgsConstructor
public class ActivityApplyRepositoryImpl implements ActivityApplyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ActivityApplyResponse> getApplies(Long activityId) {
        return queryFactory
                .select(new QActivityApplyResponse(
                        activityApply.id,
                        member.loginId,
                        member.name,
                        member.avatar.major))
                .from(activityApply)
                .where(activityApply.activityId.eq(activityId))
                .innerJoin(activityApply.member, member)
                .orderBy(activityApply.applyTime.desc())
                .fetch();
    }

    @Override
    public void deleteAll(Long activityId) {
        queryFactory
                .delete(activityApply)
                .where(activityApply.activityId.eq(activityId))
                .execute();
    }

    @Override
    public boolean isApplied(Long memberId, Long activityId) {
        return queryFactory
                .selectFrom(activityApply)
                .where(activityApply.member.id.eq(memberId),
                        activityApply.activityId.eq(activityId))
                .fetchFirst() != null;
    }

    @Override
    public void deleteByIds(Long memberId, Long activityId) {
        queryFactory
            .delete(activityApply)
            .where(activityApply.member.id.eq(memberId),
                    activityApply.activityId.eq(activityId))
            .execute();
    }
}