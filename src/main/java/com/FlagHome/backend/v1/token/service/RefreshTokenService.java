package com.FlagHome.backend.v1.token.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.v1.token.dto.TokenRequest;
import com.FlagHome.backend.v1.token.dto.TokenResponse;
import com.FlagHome.backend.v1.token.entity.RefreshToken;
import com.FlagHome.backend.v1.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtilizer jwtUtilizer;

    public void issueToken(String key, String value) {
        RefreshToken refreshToken = RefreshToken.builder()
                .key(key)
                .value(value)
                .expiredAt(LocalDateTime.now().plusWeeks(1))
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    public TokenResponse reissueToken(TokenRequest tokenRequest) {
        if (!jwtUtilizer.validateToken(tokenRequest.getRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Authentication authentication = jwtUtilizer.getAuthentication(tokenRequest.getAccessToken());
        RefreshToken refreshToken = findToken(authentication.getName());

        if (!StringUtils.equals(refreshToken.getValue(), tokenRequest.getRefreshToken())) {
            throw new CustomException(ErrorCode.TOKEN_NOT_MATCH);
        }

        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);
        return tokenResponse;
    }

    public RefreshToken findToken(String key) {
        return refreshTokenRepository.findFirstByKeyOrderByIdDesc(key)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_TOKEN));
    }
}
