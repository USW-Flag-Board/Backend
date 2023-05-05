package com.FlagHome.backend.module.post.reply.repository;

import com.FlagHome.backend.module.post.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {
    /**
     * Version 1
     */
    /* @Modifying
    @Query(value = "truncate table reply", nativeQuery = true)
    void truncateMyTable(); */
}