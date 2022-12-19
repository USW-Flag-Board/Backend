package com.FlagHome.backend.v1.member.repository;


import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.util.SecurityUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.FlagHome.backend.v1.member.entity.QMember.member;

@Repository
@Transactional
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public void withdraw(Long memberId) {
        queryFactory
                .update(member)
                .set(member.status, Status.WITHDRAW)
                .where(member.id.eq(SecurityUtils.getMemberId()))
                .execute();
    }
}
