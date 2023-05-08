package com.Flaground.backend.module.post.domain.repository;

import com.Flaground.backend.module.post.domain.Like;
import com.Flaground.backend.module.post.domain.Likeable;

import java.util.Optional;

public interface LikeRepositoryCustom {
    boolean existsByIds(Long memberId, Likeable likeable);
    Optional<Like> findByIds(Long memberId, Likeable likeable);
}
