package com.FlagHome.backend.domain.withdrawal.repository;

import com.FlagHome.backend.domain.withdrawal.entity.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
}
