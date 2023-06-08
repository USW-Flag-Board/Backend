package com.Flaground.backend.module.activity.domain.repository.implementation;

import com.Flaground.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipateResponse;
import com.Flaground.backend.module.activity.controller.dto.response.QParticipantResponse;
import com.Flaground.backend.module.activity.controller.dto.response.QParticipateResponse;
import com.Flaground.backend.module.activity.domain.repository.MemberActivityRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Flaground.backend.module.activity.domain.QActivity.activity;
import static com.Flaground.backend.module.activity.domain.QMemberActivity.memberActivity;
import static com.Flaground.backend.module.member.domain.QMember.member;

@RequiredArgsConstructor
public class MemberActivityRepositoryImpl implements MemberActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ParticipateResponse> getActivitiesByLoginId(String loginId) {
        return queryFactory
                .select(new QParticipateResponse(
                        activity.id,
                        activity.name,
                        activity.createdAt.year(),
                        activity.info.semester,
                        activity.status))
                .from(memberActivity)
                .innerJoin(memberActivity.member, member)
                .innerJoin(memberActivity.activity, activity)
                .where(member.loginId.eq(loginId))
                .fetch();
    }

    @Override
    public List<ParticipantResponse> getParticipantOfActivity(Long activityId) {
        return queryFactory
                .select(new QParticipantResponse(
                        member.name,
                        member.loginId,
                        member.avatar.major))
                .from(memberActivity)
                .innerJoin(memberActivity.member, member)
                .where(memberActivity.activity.id.eq(activityId))
                .fetch();
    }

    @Override
    public void deleteAllByActivity(Long activityId) {
        queryFactory.delete(memberActivity)
                .where(memberActivity.activity.id.eq(activityId))
                .execute();
    }
}
