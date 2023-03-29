package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.controller.dto.LoginLogResponse;
import com.FlagHome.backend.domain.member.controller.dto.QLoginLogResponse;
import com.FlagHome.backend.domain.member.controller.dto.QSearchMemberResponse;
import com.FlagHome.backend.domain.member.controller.dto.SearchMemberResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.domain.member.entity.QMember.member;
import static com.querydsl.core.types.dsl.Expressions.asString;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> getAllSleepMembers() {
        final LocalDateTime limit = LocalDateTime.now().minusDays(7);

        return queryFactory
                .selectFrom(member)
                .where(member.lastLoginTime.before(limit))
                .fetch();
    }

    @Override
    public List<String> getAllBeforeSleepEmails() {
        final LocalDateTime limit = LocalDateTime.now().minusDays(6);

        return queryFactory
                .select(member.email)
                .from(member)
                .where(member.lastLoginTime.before(limit))
                .fetch();
    }

    @Override
    public List<Member> getMembersByLoginIdList(List<String> loginIdList) {
        return queryFactory
                .selectFrom(member)
                .where(member.loginId.in(loginIdList))
                .fetch();
    }

    @Override
    public List<LoginLogResponse> getAllLoginLogs() {
        return queryFactory
                .select(new QLoginLogResponse(
                        member.id,
                        member.name,
                        member.lastLoginTime))
                .from(member)
                .fetch();
    }

    @Override
    public List<SearchMemberResponse> getSearchResultsByName(String name) {
        return queryFactory
                .select(new QSearchMemberResponse(
                        asString(name).as(member.name),
                        member.major))
                .from(member)
                .where(member.name.eq(name))
                .fetch();
    }
}