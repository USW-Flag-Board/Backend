package com.FlagHome.backend.domain.member.sleeping.repository;

import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SleepingRepository extends JpaRepository<Sleeping, Long>, SleepingRepositoryCustom {
    Optional<Sleeping> findById(Long id);
    Optional<Sleeping> findByLoginId(String loginId);
}
