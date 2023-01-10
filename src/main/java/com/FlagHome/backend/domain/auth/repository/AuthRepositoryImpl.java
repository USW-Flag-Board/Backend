package com.FlagHome.backend.domain.auth.repository;

import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public void deleteAllNotProceedAuthInformation(List<AuthInformation> authInformationList) {
        List<Long> authInformationIdList = authInformationList.stream()
                .map(AuthInformation::getId)
                .collect(Collectors.toList());

        queryFactory
                .delete(authInformation)
                .where(authInformation.id.in(authInformationIdList))
                .execute();
    }
}
