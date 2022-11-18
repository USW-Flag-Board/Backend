package com.FlagHome.backend.v1.log.repository;

import com.FlagHome.backend.v1.log.entity.PostLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PostLogRepository extends JpaRepository<PostLog, Long> {
}
