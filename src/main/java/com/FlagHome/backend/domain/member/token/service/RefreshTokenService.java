package com.FlagHome.backend.domain.member.token.service;

import com.FlagHome.backend.domain.member.token.entity.Token;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.domain.member.token.dto.TokenResponse;
import com.FlagHome.backend.domain.member.token.entity.RefreshToken;
import com.FlagHome.backend.domain.member.token.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
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
        Token refreshToken = RefreshToken.of(key, value);
        return tokenRepository.save(refreshToken);
    }

    @Transactional
    public TokenResponse reissueToken(String accessToken, String refreshToken) {
        if (!jwtUtilizer.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
        Token findRefreshToken = findToken(authentication.getName());

        if (findRefreshToken.isNotEqualTo(refreshToken)) {
            throw new CustomException(ErrorCode.TOKEN_NOT_MATCH);
        }

        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);
        findRefreshToken.renewValue(tokenResponse.getRefreshToken(), LocalDateTime.now().plusWeeks(1));

        return tokenResponse;
    }

    @Override
    public Token findToken(String key) {
        return tokenRepository.findFirstByKeyOrderByIdDesc(key)
                .orElseThrow(() -> new CustomException(ErrorCode.NONE_AUTHORIZATION_TOKEN));
    }
}