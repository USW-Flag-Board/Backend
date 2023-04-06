package com.FlagHome.backend.domain.reply.repository;

import com.FlagHome.backend.domain.reply.controller.dto.QReplyResponse;
import com.FlagHome.backend.domain.reply.controller.dto.ReplyResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.FlagHome.backend.domain.member.entity.QMember.member;
import static com.FlagHome.backend.domain.post.entity.QPost.post;
import static com.FlagHome.backend.domain.reply.entity.QReply.reply;


@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom{
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
        return null;

    }
}
