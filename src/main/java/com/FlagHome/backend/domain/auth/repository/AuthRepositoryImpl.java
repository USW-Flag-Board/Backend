package com.FlagHome.backend.domain.auth.repository;

import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.FlagHome.backend.domain.auth.entity.QAuthInformation.authInformation;


@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<AuthInformation> getAllNotProceedAuthInformation() {
        final LocalDateTime limit = LocalDateTime.now().minusMinutes(10);

        return queryFactory
                .selectFrom(authInformation)
                .where(authInformation.createdAt.before(limit),
                        authInformation.isAuthorizedCrew.eq(false))
                .fetch();
    }

    @Override
    public List<AuthInformation> getAllNeedApprovalAuthInformation() {
        return queryFactory
                .selectFrom(authInformation)
                .where(authInformation.isAuthorizedCrew.eq(true))
                .fetch();
    }
}
