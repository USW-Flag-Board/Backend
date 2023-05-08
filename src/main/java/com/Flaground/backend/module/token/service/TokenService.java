package com.Flaground.backend.module.token.service;

import com.Flaground.backend.module.token.domain.Token;

public interface TokenService {
    Token issueToken(String key, String value);

    Token findToken(String key);
}
