package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.domain.member.entity.QMember.member;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> getAllSleepMembers() {
        final LocalDateTime limit = LocalDateTime.now().minusDays(7);
        return queryFactory
                .selectFrom(member)
                .where()
                .fetch();
    }

    @Override
    public List<Member> getMembersByLoginId(List<String> loginIdList) {
        return queryFactory
                .selectFrom(member)
                .where(member.loginId.in(loginIdList))
                .fetch();
    }
}