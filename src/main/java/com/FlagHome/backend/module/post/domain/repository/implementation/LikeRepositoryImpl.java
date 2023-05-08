package com.FlagHome.backend.module.post.domain.repository.implementation;

import com.FlagHome.backend.module.post.domain.Like;
import com.FlagHome.backend.module.post.domain.repository.LikeRepositoryCustom;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.FlagHome.backend.module.post.domain.QLike.like;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public boolean existsByIds(Long memberId, Long likeableId) {
        return queryFactory
                .selectFrom(like)
                .where(like.memberId.eq(memberId),
                        like.likeableId.eq(likeableId))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Like> findByIds(Long memberId, Long likeableId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(like)
                .where(like.memberId.eq(memberId),
                        like.likeableId.eq(likeableId))
                .fetchOne());
    }
}
