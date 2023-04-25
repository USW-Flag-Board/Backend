package com.FlagHome.backend.domain.token.repository;

import com.FlagHome.backend.domain.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findFirstByKeyOrderByIdDesc(String key);
}
