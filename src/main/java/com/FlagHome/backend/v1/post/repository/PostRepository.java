package com.FlagHome.backend.v1.post.repository;

import com.FlagHome.backend.v1.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
