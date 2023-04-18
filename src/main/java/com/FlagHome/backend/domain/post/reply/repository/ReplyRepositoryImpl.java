package com.FlagHome.backend.domain.post.reply.repository;

import com.FlagHome.backend.domain.post.controller.dto.response.QReplyResponse;
import com.FlagHome.backend.domain.post.controller.dto.response.ReplyResponse;
import com.FlagHome.backend.domain.post.reply.entity.ReplyStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.FlagHome.backend.domain.member.entity.QMember.member;
import static com.FlagHome.backend.domain.post.reply.entity.QReply.reply;
import static com.querydsl.core.types.dsl.Expressions.cases;


@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom {
    private static final String INACCESSIBLE_CONTENT = "삭제된 댓글입니다.";
    private static final int BEST_REPLY_STANDARD = 5;
    private final JPAQueryFactory queryFactory;

    /**
     * Version 1
     */
    /*@Override
    public List<Reply> findByPostId(long postId) {
        return jpaQueryFactory
                .selectFrom(reply)
                .where(reply.post.id.eq(postId))
                .fetch();
    }

    @Override
    public List<Reply> findByPostIdAndReplyGroup(long postId, long replyGroup) {
        return jpaQueryFactory
                .selectFrom(reply)
                .where(reply.post.id.eq(postId)
                        .and(reply.replyGroup.eq(replyGroup)))
                .fetch();
    }*/

    /**
     * Version 2
     */
    @Override
    public List<ReplyResponse> getAllReplies(Long postId) {
        return queryFactory
                .select(new QReplyResponse(
                        reply.id,
                        member.avatar.nickname,
                        member.avatar.profileImage,
                        cases()
                            .when(reply.status.eq(ReplyStatus.BANNED))
                            .then(INACCESSIBLE_CONTENT)
                            .otherwise(reply.content),
                        reply.likeCount,
                        reply.createdAt,
                        reply.createdAt.ne(reply.updatedAt)))
                .from(reply)
                .innerJoin(reply.member, member)
                .where(reply.post.id.eq(postId))
                .orderBy(reply.createdAt.asc())
                .fetch();
    }

    @Override
    public ReplyResponse getBestReply(Long postId) {
        return queryFactory
                .select(new QReplyResponse(
                        reply.id,
                        member.avatar.nickname,
                        member.avatar.profileImage,
                        reply.content,
                        reply.likeCount,
                        reply.createdAt,
                        reply.createdAt.ne(reply.updatedAt)))
                .from(reply)
                .innerJoin(reply.member, member)
                .where(reply.post.id.eq(postId),
                        reply.likeCount.goe(BEST_REPLY_STANDARD),
                        reply.status.ne(ReplyStatus.BANNED))
                .fetchOne();
    }
}
