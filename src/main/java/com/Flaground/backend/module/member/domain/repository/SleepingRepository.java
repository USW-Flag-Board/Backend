package com.Flaground.backend.module.member.domain.repository;

import com.Flaground.backend.module.member.domain.Sleeping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SleepingRepository extends JpaRepository<Sleeping, Long>, SleepingRepositoryCustom {
    Optional<Sleeping> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
}
