package com.Flaground.backend.module.post.domain.repository;

import com.Flaground.backend.module.post.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {
}
