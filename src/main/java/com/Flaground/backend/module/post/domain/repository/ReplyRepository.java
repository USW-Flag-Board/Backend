package com.Flaground.backend.module.post.domain.repository;

import com.Flaground.backend.module.post.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
