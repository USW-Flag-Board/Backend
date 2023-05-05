package com.FlagHome.backend.module.token.service;

import com.FlagHome.backend.module.token.entity.FindRequestToken;
import com.FlagHome.backend.module.token.entity.Token;
import com.FlagHome.backend.module.token.repository.TokenRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindRequestTokenService implements TokenService {
    private final TokenRepository tokenRepository;
    @Override
    public Token issueToken(String key, String value) {
        Token findRequestToken = FindRequestToken.of(key, value);
        return tokenRepository.save(findRequestToken);
    }

    @Override
    public Token findToken(String key) {
        return tokenRepository.findFirstByKeyOrderByIdDesc(key)
                .orElseThrow(() -> new CustomException(ErrorCode.FIND_REQUEST_NONE));
    }
}
