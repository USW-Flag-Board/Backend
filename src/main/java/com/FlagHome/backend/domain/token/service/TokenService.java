package com.FlagHome.backend.domain.token.service;

import com.FlagHome.backend.domain.token.entity.Token;

public interface TokenService {
    Token issueToken(String key, String value);

    Token findToken(String key);
}
