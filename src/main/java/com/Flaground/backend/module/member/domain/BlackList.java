package com.Flaground.backend.module.member.domain;

import com.Flaground.backend.module.member.domain.enums.BlackType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "period")
    private BlackType blackType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public BlackList(String email, BlackType blackType) {
        this.email = email;
        this.blackType = blackType;
        this.createdAt = LocalDateTime.now();
    }

    public static BlackList ban(String email) {
        return BlackList.builder()
                .email(email)
                .blackType(BlackType.BAN)
                .build();
    }

    public static BlackList suspend(String email) {
        return BlackList.builder()
                .email(email)
                .blackType(BlackType.SUSPEND)
                .build();
    }
}
