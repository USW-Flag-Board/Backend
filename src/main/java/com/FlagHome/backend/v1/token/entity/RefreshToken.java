package com.FlagHome.backend.v1.token.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_key")
    private String key;

    @Column(name = "token_value")
    private String value;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public void resetValue(String value, LocalDateTime expiredAt) {
        this.value = value;
        this.expiredAt = expiredAt;
    }
}
