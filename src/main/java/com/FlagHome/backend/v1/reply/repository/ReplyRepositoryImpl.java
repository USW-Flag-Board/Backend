package com.FlagHome.backend.v1.reply.repository;

import com.FlagHome.backend.v1.reply.entity.Reply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.FlagHome.backend.v1.reply.entity.QReply.reply;

@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
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
    }
}
