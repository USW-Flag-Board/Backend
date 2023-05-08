package com.Flaground.backend.module.token.service;

import com.Flaground.backend.module.token.domain.RecoveryToken;
import com.Flaground.backend.module.token.domain.Token;
import com.Flaground.backend.module.token.domain.repository.TokenRepository;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecoveryTokenService implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Token issueToken(String key, String value) {
        Token findRequestToken = RecoveryToken.of(key, value);
        return tokenRepository.save(findRequestToken);
    }

    @Override
    public Token findToken(String key) {
        return tokenRepository.findFirstByKeyOrderByIdDesc(key)
                .orElseThrow(() -> new CustomException(ErrorCode.FIND_REQUEST_NONE));
    }
}
