package com.FlagHome.backend.domain.member.token.repository;

import com.FlagHome.backend.domain.member.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findFirstByKeyOrderByIdDesc(String key);
}
