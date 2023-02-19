package com.FlagHome.backend.domain.token.entity;


import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "token", indexes = @Index(columnList = "token_key"))
public abstract class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_key")
    private String key;

    @Column(name = "token_value")
    private String value;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public Token(String key, String value, LocalDateTime expiredAt) {
        this.key = key;
        this.value = value;
        this.expiredAt = expiredAt;
    }

    public boolean isNotEqualTo(String value) {
        return !StringUtils.equals(this.value, value);
    }

    public void updateValue(String value, LocalDateTime expiredAt) {
        this.value = value;
        this.expiredAt = expiredAt;
    }

    public void validateExpireTime() {
        if (this.expiredAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}
