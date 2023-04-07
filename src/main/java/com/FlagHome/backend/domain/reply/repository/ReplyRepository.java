package com.FlagHome.backend.domain.reply.repository;

import com.FlagHome.backend.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {
    /**
     * Version 1
     */
    /* @Modifying
    @Query(value = "truncate table reply", nativeQuery = true)
    void truncateMyTable(); */
}
