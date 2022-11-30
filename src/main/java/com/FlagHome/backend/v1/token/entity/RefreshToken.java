package com.FlagHome.backend.v1.token.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String key;

    @Column
    private String value;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

}
