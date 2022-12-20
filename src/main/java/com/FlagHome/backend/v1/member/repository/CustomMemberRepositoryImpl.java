package com.FlagHome.backend.v1.member.repository;

import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.util.SecurityUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.FlagHome.backend.v1.member.entity.QMember.member;

@Repository
@Transactional
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {
    private final JPAQueryFactory queryFactory;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void withdraw(Long memberId) {
        queryFactory
                .update(member)
                .set(member.status, Status.WITHDRAW)
                .where(member.id.eq(memberId))
                .execute();
    }

    @Override
    public void updatePassword(Long memberId, String newPassword) {
        queryFactory
                .update(member)
                .set(member.password, passwordEncoder.encode(newPassword))
                .where(member.id.eq(memberId))
                .execute();
    }
}
