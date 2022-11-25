package com.FlagHome.backend.v1.token.service;

import com.FlagHome.backend.v1.token.entity.RefreshToken;
import com.FlagHome.backend.v1.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void issueToken(String key, String value) {
        RefreshToken refreshToken = RefreshToken.builder()
                .key(key)
                .value(value)
                .expiredAt(LocalDateTime.now().plusWeeks(1))
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}
