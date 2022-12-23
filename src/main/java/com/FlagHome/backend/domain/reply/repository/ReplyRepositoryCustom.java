package com.FlagHome.backend.domain.reply.repository;

import com.FlagHome.backend.domain.reply.entity.Reply;

import java.util.List;

public interface ReplyRepositoryCustom {
    List<Reply> findByPostId(long postId);
    List<Reply> findByPostIdAndReplyGroup(long postId, long replyGroup);
}
