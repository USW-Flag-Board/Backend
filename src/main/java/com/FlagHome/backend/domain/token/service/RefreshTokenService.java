package com.FlagHome.backend.domain.token.service;

import com.FlagHome.backend.domain.token.entity.Token;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.domain.token.dto.TokenRequest;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.domain.token.entity.RefreshToken;
import com.FlagHome.backend.domain.token.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements TokenService {
    private final TokenRepository tokenRepository;

    private final JwtUtilizer jwtUtilizer;

    @Override
    @Transactional
    public Token issueToken(String key, String value) {
        Token refreshToken = RefreshToken.builder()
                .key(key)
                .value(value)
                .expiredAt(LocalDateTime.now().plusWeeks(1))
                .build();

        return tokenRepository.save(refreshToken);
    }

    @Transactional
    public TokenResponse reissueToken(TokenRequest tokenRequest) {
        if (!jwtUtilizer.validateToken(tokenRequest.getRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Authentication authentication = jwtUtilizer.getAuthentication(tokenRequest.getAccessToken());
        Token refreshToken = findToken(authentication.getName());

        if (!StringUtils.equals(refreshToken.getValue(), tokenRequest.getRefreshToken())) {
            throw new CustomException(ErrorCode.TOKEN_NOT_MATCH);
        }

        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);
        refreshToken.updateValue(tokenResponse.getRefreshToken(), LocalDateTime.now().plusWeeks(1));

        return tokenResponse;
    }

    @Override
    public Token findToken(String key) {
        return tokenRepository.findFirstByKeyOrderByIdDesc(key)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_TOKEN));
    }
}
