package com.FlagHome.backend.domain.token.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class FindRequestToken extends Token {

    @Builder
    public FindRequestToken(String key, String value, LocalDateTime expiredAt) {
        super(key, value, expiredAt);
    }
}
