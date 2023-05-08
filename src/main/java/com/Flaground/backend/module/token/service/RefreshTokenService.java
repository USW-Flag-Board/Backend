package com.Flaground.backend.module.token.service;

import com.Flaground.backend.module.token.domain.Token;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.global.jwt.JwtUtilizer;
import com.Flaground.backend.module.token.dto.TokenResponse;
import com.Flaground.backend.module.token.domain.RefreshToken;
import com.Flaground.backend.module.token.domain.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService implements TokenService {
    private final TokenRepository tokenRepository;
    private final JwtUtilizer jwtUtilizer;

    @Override
    public Token issueToken(String key, String value) {
        Token refreshToken = RefreshToken.of(key, value);
        return tokenRepository.save(refreshToken);
    }

    public TokenResponse reissueToken(String accessToken, String refreshToken) {
        isValidToken(refreshToken);

        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
        Token findRefreshToken = findToken(authentication.getName());
        findRefreshToken.isSameToken(refreshToken);

        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);
        findRefreshToken.renewValue(tokenResponse.getRefreshToken(), LocalDateTime.now().plusWeeks(1));

        return tokenResponse;
    }

    @Override
    public Token findToken(String key) {
        return tokenRepository.findFirstByKeyOrderByIdDesc(key)
                .orElseThrow(() -> new CustomException(ErrorCode.NONE_AUTHORIZATION_TOKEN));
    }

    private void isValidToken(String refreshToken) {
        if (!jwtUtilizer.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}
