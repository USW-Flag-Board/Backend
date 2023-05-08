package com.Flaground.backend.module.post.domain.repository;

import com.Flaground.backend.module.post.domain.Like;

import java.util.Optional;

public interface LikeRepositoryCustom {
    boolean existsByIds(Long memberId, Long likeableId);
    Optional<Like> findByIds(Long memberId, Long likeableId);
}
