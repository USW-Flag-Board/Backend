package com.FlagHome.backend.domain.withdrawal.repository;

import com.FlagHome.backend.domain.sleeping.entity.Sleeping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalRepository extends JpaRepository<Sleeping, Long> {
}