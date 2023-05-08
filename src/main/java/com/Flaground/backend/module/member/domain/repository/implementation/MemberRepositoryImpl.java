package com.Flaground.backend.module.member.domain.repository.implementation;

import com.Flaground.backend.module.member.controller.dto.response.*;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.Flaground.backend.module.member.domain.QMember.member;
import static com.querydsl.core.types.dsl.Expressions.asString;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> getDeactivateMembers() {
        final LocalDateTime limit = LocalDateTime.now().minusDays(7);

        return queryFactory
                .selectFrom(member)
                .where(member.updatedAt.before(limit))
                .fetch();
    }

    @Override
    public List<String> getAllBeforeSleepEmails() {
        final LocalDateTime limit = LocalDateTime.now().minusDays(6);

        return queryFactory
                .select(member.email)
                .from(member)
                .where(member.updatedAt.before(limit))
                .fetch();
    }

    @Override
    public List<Member> getMembersByLoginIds(List<String> loginIdList) {
        return queryFactory
                .selectFrom(member)
                .where(member.loginId.in(loginIdList))
                .fetch();
    }

    @Override
    public AvatarResponse getAvatar(String loginId) {
        return queryFactory
                .select(new QAvatarResponse(
                        asString(loginId).as(member.loginId),
                        member.avatar.nickname,
                        member.avatar.bio,
                        member.avatar.profileImage))
                .from(member)
                .where(member.loginId.eq(loginId))
                .fetchOne();
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
    public List<LoginLogResponse> getAllLoginLogs() {
        return queryFactory
                .select(new QLoginLogResponse(
                        member.id,
                        member.name,
                        member.updatedAt))
                .from(member)
                .fetch();
    }

    @Override
    public List<SearchMemberResponse> searchMemberByName(String name) {
        return queryFactory
                .select(new QSearchMemberResponse(
                        asString(name).as(member.name),
                        member.avatar.major))
                .from(member)
                .where(member.name.contains(name))
                .fetch();
    }
}