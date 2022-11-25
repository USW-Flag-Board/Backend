package com.FlagHome.backend.v1.token.repository;

import com.FlagHome.backend.v1.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

}
