package com.Flaground.backend.module.member.domain.repository.implementation;

import com.Flaground.backend.module.member.controller.dto.response.*;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.enums.MemberStatus;
import com.Flaground.backend.module.member.domain.repository.MemberRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.Flaground.backend.module.member.domain.QMember.member;
import static com.querydsl.core.types.dsl.Expressions.asString;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> getDeactivateMembers() {
        final LocalDateTime limit = LocalDateTime.now().minusDays(7);

        return queryFactory
                .selectFrom(member)
                .where(member.updatedAt.before(limit),
                        isAvailableMember())
                .fetch();
    }

    @Override
    public List<String> getDeactivableMemberEmails() {
        final LocalDateTime limit = LocalDateTime.now().minusDays(6);

        return queryFactory
                .select(member.email)
                .from(member)
                .where(member.updatedAt.before(limit),
                        isAvailableMember())
                .fetch();
    }

    @Override
    public MyProfileResponse getMyProfile(Long memberId) {
        return queryFactory
                .select(new QMyProfileResponse(
                        member.avatar.nickname,
                        member.avatar.bio,
                        member.avatar.profileImage,
                        member.loginId,
                        member.name,
                        member.email,
                        member.avatar.major,
                        member.avatar.studentId))
                .from(member)
                .where(member.id.eq(memberId))
                .fetchOne();
    }

    @Override
    public List<LoginLogResponse> getLoginLogs() {
        return queryFactory
                .select(new QLoginLogResponse(
                        member.id,
                        member.name,
                        member.updatedAt))
                .from(member)
                .where(isAvailableMember())
                .fetch();
    }

    @Override
    public List<SearchMemberResponse> searchMemberByName(String name) {
        return queryFactory
                .select(new QSearchMemberResponse(
                        asString(name).as(member.name),
                        member.avatar.major))
                .from(member)
                .where(member.name.contains(name),
                        isAvailableMember())
                .fetch();
    }

    private BooleanExpression isAvailableMember() {
        return member.status.in(MemberStatus.NORMAL, MemberStatus.WATCHING);
    }
}