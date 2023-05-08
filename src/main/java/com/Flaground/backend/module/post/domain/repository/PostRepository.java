package com.Flaground.backend.module.post.domain.repository;

import com.Flaground.backend.module.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
