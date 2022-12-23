package com.FlagHome.backend.domain.post.repository;

import com.FlagHome.backend.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
