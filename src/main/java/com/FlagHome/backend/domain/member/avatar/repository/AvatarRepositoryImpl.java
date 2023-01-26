package com.FlagHome.backend.domain.member.avatar.repository;

import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AvatarRepositoryImpl implements AvatarRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public AvatarResponse getAvatar() {
        return null;
    }
}
