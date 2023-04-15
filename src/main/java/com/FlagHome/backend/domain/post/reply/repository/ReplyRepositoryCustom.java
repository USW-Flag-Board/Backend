package com.FlagHome.backend.domain.post.reply.repository;

import com.FlagHome.backend.domain.post.controller.dto.ReplyResponse;

import java.util.List;

public interface ReplyRepositoryCustom {
    /**
     * Version 1
     */
    /*List<Reply> findByPostId(long postId);
    List<Reply> findByPostIdAndReplyGroup(long postId, long replyGroup);*/
    /**
     * Version 2
     */
    List<ReplyResponse> getAllReplies(Long postId);

    ReplyResponse getBestReply(Long postId);
}
