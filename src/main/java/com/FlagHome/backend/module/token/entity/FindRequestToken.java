package com.FlagHome.backend.module.token.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindRequestToken extends Token {

    @Builder
    public FindRequestToken(String key, String value, LocalDateTime expiredAt) {
        super(key, value, expiredAt);
    }

    public static FindRequestToken of(String key, String value) {
        return FindRequestToken.builder()
                .key(key)
                .value(value)
                .expiredAt(LocalDateTime.now().plusMinutes(3)) // 3분 내로 인증
                .build();
    }
}
