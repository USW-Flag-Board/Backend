package com.Flaground.backend.module.auth.domain.repository.implementation;

import com.Flaground.backend.module.admin.controller.dto.ApproveSignUpResponse;
import com.Flaground.backend.module.admin.controller.dto.QApproveSignUpResponse;
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
    public List<ApproveSignUpResponse> getAllSignUpRequests() {
        return queryFactory
                .select(new QApproveSignUpResponse(
                        authInformation.id,
                        authInformation.name,
                        authInformation.email,
                        authInformation.major))
                .from(authInformation)
                .where(authInformation.isAuthorizedCrew.eq(true))
                .fetch();
    }
}
