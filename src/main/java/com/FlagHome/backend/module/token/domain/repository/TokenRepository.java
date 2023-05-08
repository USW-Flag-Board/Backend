package com.FlagHome.backend.module.token.domain.repository;

import com.FlagHome.backend.module.token.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findFirstByKeyOrderByIdDesc(String key);
}
