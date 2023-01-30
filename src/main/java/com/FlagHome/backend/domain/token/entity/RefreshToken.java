package com.FlagHome.backend.domain.token.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class RefreshToken extends Token {

    @Builder
    public RefreshToken(String key, String value, LocalDateTime expiredAt) {
        super(key, value, expiredAt);
    }
}
