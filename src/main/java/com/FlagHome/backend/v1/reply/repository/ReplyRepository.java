package com.FlagHome.backend.v1.reply.repository;

import com.FlagHome.backend.v1.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    // Contribute를 위한 테스트 커밋용 (2022.10.18 윤희승)

}
