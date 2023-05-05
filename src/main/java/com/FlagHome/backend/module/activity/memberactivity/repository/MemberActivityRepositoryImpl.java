package com.FlagHome.backend.module.activity.memberactivity.repository;

import com.FlagHome.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.ParticipateResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.QParticipantResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.QParticipateResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.FlagHome.backend.module.activity.entity.QActivity.activity;
import static com.FlagHome.backend.module.activity.memberactivity.entity.QMemberActivity.memberActivity;
import static com.FlagHome.backend.module.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberActivityRepositoryImpl implements MemberActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ParticipateResponse> getAllActivitiesOfMember(String loginId) {
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
    public List<ParticipantResponse> getAllParticipantByActivityId(Long activityId) {
        return queryFactory
                .select(new QParticipantResponse(
                        member.name,
                        member.loginId,
                        member.avatar.major))
                .from(memberActivity)
                .innerJoin(memberActivity.activity, activity)
                .innerJoin(memberActivity.member, member)
                .where(memberActivity.activity.id.eq(activityId))
                .fetch();
    }
}
