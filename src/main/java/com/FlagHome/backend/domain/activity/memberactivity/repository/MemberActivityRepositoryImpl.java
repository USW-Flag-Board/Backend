package com.FlagHome.backend.domain.activity.memberactivity.repository;

import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipantResponse;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.activity.memberactivity.dto.QParticipantResponse;
import com.FlagHome.backend.domain.activity.memberactivity.dto.QParticipateResponse;
import com.FlagHome.backend.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.FlagHome.backend.domain.activity.entity.QActivity.activity;
import static com.FlagHome.backend.domain.activity.memberactivity.entity.QMemberActivity.memberActivity;
import static com.FlagHome.backend.domain.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberActivityRepositoryImpl implements MemberActivityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteAllByActivityId(long activityId) {
        queryFactory.delete(memberActivity)
                .where(memberActivity.activity.id.eq(activityId))
                .execute();
    }

    @Override
    public List<ParticipateResponse> getAllActivitiesOfMember(String loginId) {
        return queryFactory
                .select(new QParticipateResponse(
                        activity.id,
                        activity.name,
                        activity.createdAt.year(), // Long 타입 해결하기 (메모리 누수)
                        activity.season,
                        activity.status))
                .from(memberActivity)
                .innerJoin(memberActivity.member, member)
                .innerJoin(memberActivity.activity, activity)
                .where(member.loginId.eq(loginId))
                .fetch();
    }

    @Override
    public List<ParticipantResponse> getAllParticipantByActivityId(long activityId) {
        return queryFactory
                .select(new QParticipantResponse(
                        member.name,
                        member.loginId,
                        member.major))
                .from(memberActivity)
                .innerJoin(memberActivity.activity, activity)
                .innerJoin(memberActivity.member, member)
                .where(memberActivity.activity.id.eq(activityId))
                .fetch();
    }

    @Override
    public Member findMemberOfActivityByLoginId(long activityId, String loginId) {
        return queryFactory.select(member)
                .from(memberActivity)
                .innerJoin(memberActivity.member, member)
                .where(member.loginId.eq(loginId),
                        memberActivity.activity.id.eq(activityId))
                .fetchOne();
    }
}
