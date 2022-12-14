package com.FlagHome.backend.domain.reply.repository;

import com.FlagHome.backend.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {
    @Modifying
    @Query(value = "truncate table reply", nativeQuery = true)
    void truncateMyTable();
}
