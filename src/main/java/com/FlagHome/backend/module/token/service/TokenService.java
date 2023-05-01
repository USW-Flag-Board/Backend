package com.FlagHome.backend.module.token.service;

import com.FlagHome.backend.module.token.entity.Token;

public interface TokenService {
    Token issueToken(String key, String value);

    Token findToken(String key);
}
