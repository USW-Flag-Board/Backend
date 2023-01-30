package com.FlagHome.backend.domain.token.service;

import com.FlagHome.backend.domain.token.entity.FindRequestToken;
import com.FlagHome.backend.domain.token.entity.Token;
import com.FlagHome.backend.domain.token.repository.TokenRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FindRequestTokenService implements TokenService {
    private final TokenRepository tokenRepository;
    @Override
    public Token issueToken(String key, String value) {
        Token findRequestToken = FindRequestToken.builder()
                .key(key)
                .value(value)
                .expiredAt(LocalDateTime.now().plusMinutes(3))
                .build();

        return tokenRepository.save(findRequestToken);
    }

    @Override
    public Token findToken(String key) {
        return tokenRepository.findFirstByKeyOrderByIdDesc(key)
                .orElseThrow(() -> new CustomException(ErrorCode.FIND_REQUEST_NONE));
    }
}
