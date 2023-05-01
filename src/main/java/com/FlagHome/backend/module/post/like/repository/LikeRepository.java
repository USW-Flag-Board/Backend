package com.FlagHome.backend.module.post.like.repository;

import com.FlagHome.backend.module.post.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeRepositoryCustom {
}
