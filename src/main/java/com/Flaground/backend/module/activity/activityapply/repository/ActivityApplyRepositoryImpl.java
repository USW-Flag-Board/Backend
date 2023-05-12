package com.Flaground.backend.module.activity.activityapply.repository;

import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.Flaground.backend.module.activity.controller.dto.response.QActivityApplyResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Flaground.backend.module.activity.activityapply.entity.QActivityApply.activityApply;
import static com.Flaground.backend.module.member.domain.QMember.member;

@RequiredArgsConstructor
public class ActivityApplyRepositoryImpl implements ActivityApplyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ActivityApplyResponse> getAllApplies(long activityId) {
        return queryFactory
                .select(new QActivityApplyResponse(
                        activityApply.id,
                        member.loginId,
                        member.name,
                        member.avatar.major))
                .from(activityApply)
                .where(activityApply.activity.id.eq(activityId))
                .innerJoin(activityApply.member, member)
                .orderBy(activityApply.applyTime.desc())
                .fetch();
    }

    @Override
    public void deleteAllApplies(long activityId) {
        queryFactory
                .delete(activityApply)
                .where(activityApply.activity.id.eq(activityId))
                .execute();
    }

    @Override
    public boolean isApplied(long memberId, long activityId) {
        return queryFactory
                .selectFrom(activityApply)
                .innerJoin(activityApply.member)
                .where(activityApply.member.id.eq(memberId),
                        activityApply.activity.id.eq(activityId))
                .fetchFirst() != null;
    }

    @Override
    public void deleteByMemberIdAndActivityId(long memberId, long activityId) {
        queryFactory
            .delete(activityApply)
            .where(activityApply.member.id.eq(memberId),
                    activityApply.activity.id.eq(activityId))
            .execute();
    }
}