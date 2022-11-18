package com.FlagHome.backend.v1.reply.repository;

import com.FlagHome.backend.v1.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyRepositoryCustom {
    @Modifying
    @Query(value = "truncate table reply", nativeQuery = true)
    void truncateMyTable();
}
