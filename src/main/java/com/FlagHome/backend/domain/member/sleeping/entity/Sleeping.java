package com.FlagHome.backend.domain.member.sleeping.entity;

import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sleeping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "login_id")
    private String loginId;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String name;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Builder
    public Sleeping(Member member, String loginId, String password, String email, String name) {
        this.member = member;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.expiredAt = LocalDateTime.now().plusDays(60);
    }

    public static Sleeping of(Member member){
        return Sleeping.builder()
                .member(member)
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .email(member.getEmail())
                .name(member.getName())
                .build();
    }

}
