package com.FlagHome.backend.domain.token.repository;

import com.FlagHome.backend.domain.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findFirstByKeyOrderByIdDesc(String key);
}
