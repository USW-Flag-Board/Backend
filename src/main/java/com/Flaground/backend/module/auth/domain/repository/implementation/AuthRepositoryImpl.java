package com.Flaground.backend.module.auth.domain.repository.implementation;

import com.Flaground.backend.module.auth.controller.dto.response.QSignUpRequestResponse;
import com.Flaground.backend.module.auth.controller.dto.response.SignUpRequestResponse;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.domain.repository.AuthRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.Flaground.backend.module.auth.domain.QAuthInformation.authInformation;

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
    public List<SignUpRequestResponse> getSignUpRequests() {
        return queryFactory
                .select(new QSignUpRequestResponse(
                        authInformation.id,
                        authInformation.name,
                        authInformation.email,
                        authInformation.major))
                .from(authInformation)
                .where(authInformation.isAuthorizedCrew.eq(true))
                .fetch();
    }
}
