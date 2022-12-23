package com.FlagHome.backend.domain.log.repository;

import com.FlagHome.backend.domain.log.entity.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
}
