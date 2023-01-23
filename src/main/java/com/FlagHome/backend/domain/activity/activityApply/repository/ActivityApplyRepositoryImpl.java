package com.FlagHome.backend.domain.activity.activityApply.repository;

import com.FlagHome.backend.domain.activity.activityApply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityApply.dto.QActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityApply.entity.ActivityApply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.FlagHome.backend.domain.activity.activityApply.entity.QActivityApply.activityApply;
import static com.FlagHome.backend.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class ActivityApplyRepositoryImpl implements ActivityApplyRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ActivityApplyResponse> getAllApplies(long activityId) {
        return queryFactory
                .select(new QActivityApplyResponse(
                        member.id,
                        member.loginId,
                        member.name,
                        member.major))
                .from(activityApply)
                .where(activityApply.activity.id.eq(activityId))
                .innerJoin(activityApply.member, member)
                .orderBy(activityApply.applyTime.asc())
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
    public boolean checkApply(long memberId, long activityId) {
        return queryFactory
                .selectFrom(activityApply)
                .innerJoin(activityApply.member)
                .where(activityApply.member.id.eq(memberId),
                        activityApply.activity.id.eq(activityId))
                .fetchFirst() != null;
    }

    @Override
    public ActivityApply findByMemberIdAndActivityId(long memberId, long activityId) {
        return queryFactory
                .selectFrom(activityApply)
                .where(activityApply.member.id.eq(memberId),
                        activityApply.activity.id.eq(activityId))
                .fetchOne();
    }
}