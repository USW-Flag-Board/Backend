package com.FlagHome.backend.domain.like.repository;

import com.FlagHome.backend.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
