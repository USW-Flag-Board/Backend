package com.FlagHome.backend.module.post.repository;

import com.FlagHome.backend.module.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
