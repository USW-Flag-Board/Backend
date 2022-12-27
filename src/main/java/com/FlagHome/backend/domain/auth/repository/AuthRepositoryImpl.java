package com.FlagHome.backend.domain.auth.repository;

import com.FlagHome.backend.domain.auth.entity.AuthMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.domain.auth.entity.QAuthMember.authMember;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuthMember> getAllNotProceedAuthMember() {
        return queryFactory
                .selectFrom(authMember)
                .where(authMember.createdAt.before(LocalDateTime.now().minusMinutes(10)),
                        authMember.isAuthorizedClubMember.eq(false))
                .fetch();
    }

    @Override
    public List<AuthMember> getAllAuthorizedAuthMember() {
        return queryFactory
                .selectFrom(authMember)
                .where(authMember.isAuthorizedClubMember.eq(true))
                .fetch();
    }

    @Override
    public void deleteAllNotProceedAuthMember(List<AuthMember> authMemberList) {
        for (AuthMember unAuthMember : authMemberList) {
            queryFactory
                    .delete(authMember)
                    .where(authMember.id.eq(unAuthMember.getId()))
                    .execute();
        }
    }
}
