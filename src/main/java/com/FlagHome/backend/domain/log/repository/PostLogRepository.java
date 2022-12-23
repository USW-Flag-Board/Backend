package com.FlagHome.backend.domain.log.repository;

import com.FlagHome.backend.domain.log.entity.PostLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PostLogRepository extends JpaRepository<PostLog, Long> {
}
