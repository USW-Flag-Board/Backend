package com.FlagHome.backend.module.post.like.repository;

import com.FlagHome.backend.module.post.like.entity.Like;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.FlagHome.backend.module.post.like.entity.QPostLike.postLike;
import static com.FlagHome.backend.module.post.like.entity.QReplyLike.replyLike;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public boolean isPostLiked(Long memberId, Long postId) {
        return queryFactory
                .selectFrom(postLike)
                .where(postLike.member.id.eq(memberId),
                        postLike.post.id.eq(postId))
                .fetchFirst() != null;
    }

    @Override
    public boolean isReplyLiked(Long memberId, Long replyId) {
        return queryFactory
                .selectFrom(replyLike)
                .where(replyLike.member.id.eq(memberId),
                        replyLike.reply.id.eq(replyId))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Like> findByMemberAndPost(Long memberId, Long postId) {
        Like like = queryFactory
                .selectFrom(postLike)
                .where(postLike.member.id.eq(memberId),
                        postLike.post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(like);
    }

    @Override
    public Optional<Like> findByMemberAndReply(Long memberId, Long replyId) {
        Like like = queryFactory
                .selectFrom(replyLike)
                .where(replyLike.member.id.eq(memberId),
                        replyLike.reply.id.eq(replyId))
                .fetchOne();

        return Optional.ofNullable(like);
    }
}
