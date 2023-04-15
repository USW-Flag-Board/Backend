package com.FlagHome.backend.domain.post.like.repository;

import com.FlagHome.backend.domain.post.like.entity.Like;

import java.util.Optional;

public interface LikeRepositoryCustom {
    boolean isPostLiked(Long memberId, Long postId);
    boolean isReplyLiked(Long memberId, Long replyId);

    Optional<Like> findByMemberAndPost(Long memberId, Long postId);
    Optional<Like> findByMemberAndReply(Long memberId, Long replyId);
}
