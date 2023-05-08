package com.Flaground.backend.module.token.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecoveryToken extends Token {

    @Builder
    public RecoveryToken(String key, String value, LocalDateTime expiredAt) {
        super(key, value, expiredAt);
    }

    public static RecoveryToken of(String key, String value) {
        return RecoveryToken.builder()
                .key(key)
                .value(value)
                .expiredAt(LocalDateTime.now().plusMinutes(3)) // 3분 내로 인증
                .build();
    }
}
