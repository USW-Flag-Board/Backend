package com.Flaground.backend.module.member.domain.repository.implementation;

import com.Flaground.backend.global.common.response.SearchResponse;
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
    public List<Long> getWithdrawMembers() {
        return queryFactory
                .select(member.id)
                .from(member)
                .where(member.status.eq(MemberStatus.WITHDRAW))
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
    public SearchResponse<SearchMemberResponse> searchMemberByName(String name) {
        List<SearchMemberResponse> responses = queryFactory
                .select(new QSearchMemberResponse(
                        member.loginId,
                        asString(name).as(member.name),
                        member.avatar.major))
                .from(member)
                .where(member.name.contains(name),
                        isAvailableMember())
                .fetch();

        return new SearchResponse<>(responses);
    }

    private BooleanExpression isAvailableMember() {
        return member.status.eq(MemberStatus.NORMAL);
    }
}
