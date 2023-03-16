package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.avatar.entity.QAvatar;
import com.FlagHome.backend.domain.member.dto.LoginLogResponse;
import com.FlagHome.backend.domain.member.dto.QLoginLogResponse;
import com.FlagHome.backend.domain.member.dto.SearchMemberResponse;
import com.FlagHome.backend.domain.member.entity.Member;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.domain.member.avatar.entity.QAvatar.avatar;
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
    public List<Member> getMembersByLoginId(List<String> loginIdList) {
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

/*    @Override
    public List<Member> findByMemberName(String name) {

        QAvatar qAvatar = new QAvatar("qAvatar");

        return queryFactory
                .select(member)
                .from(member)
                .where(member.name.contains(name))
                .fetch();

        queryFactory
                .select(new QAvatarResponse(
                        avatar.profileImg))
                .from(avatar)
                .innerJoin(avatar.member, member)
                .where(member.name.contains(name))
                .fetch();
    }*/

/*    @Override
    public List<SearchMemberResponse> findByMemberName(String name) {

        return (List<SearchMemberResponse>)
                queryFactory
                .select((Expression<?>) new SearchMemberResponse(member.id, member.name, member.major, avatar.profileImg))
                .from(avatar)
                .innerJoin(avatar.member, member)
                .where(member.name.contains(name))
                .fetch();
    }*/

    @Override
    public List<Member> findByMemberName(String name) {

        return queryFactory
                        .select(member)
                        .from(member)
                        .where(member.name.contains(name))
                        .fetch();
    }
}