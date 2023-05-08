package com.Flaground.backend.module.post.domain.repository.implementation;

import com.Flaground.backend.module.post.domain.Like;
import com.Flaground.backend.module.post.domain.Likeable;
import com.Flaground.backend.module.post.domain.enums.LikeType;
import com.Flaground.backend.module.post.domain.repository.LikeRepositoryCustom;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.Flaground.backend.module.post.domain.QLike.like;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public boolean existsByIds(Long memberId, Likeable likeable) {
        return queryFactory
                .selectFrom(like)
                .where(like.memberId.eq(memberId),
                        like.likeableId.eq(likeable.getId()),
                        like.likeType.eq(LikeType.from(likeable)))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Like> findByIds(Long memberId, Likeable likeable) {
        return Optional.ofNullable(queryFactory
                .selectFrom(like)
                .where(like.memberId.eq(memberId),
                        like.likeableId.eq(likeable.getId()),
                        like.likeType.eq(LikeType.from(likeable)))
                .fetchOne());
    }
}
