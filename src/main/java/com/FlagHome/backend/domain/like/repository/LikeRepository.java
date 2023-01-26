package com.FlagHome.backend.domain.like.repository;

import com.FlagHome.backend.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findLikeByUserId(long userId);
}
