package com.FlagHome.backend.module.token.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends Token {

    @Builder
    public RefreshToken(String key, String value, LocalDateTime expiredAt) {
        super(key, value, expiredAt);
    }
    
    public static RefreshToken of(String key, String value) {
        return RefreshToken.builder()
                .key(key)
                .value(value)
                .expiredAt(LocalDateTime.now().plusWeeks(1))
//                .expiredAt(LocalDateTime.now().plusMinutes(5)) // 테스트용
                .build();
    }
}
