package com.FlagHome.backend.domain.sleeping.entity;

import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sleeping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "login_id")
    private String loginId;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String name;

    @Column(name = "student_id")
    private String studentId;

    @Column
    private String major;

    @Column(name = "phone_number")
    private String phoneNumber;

    public static Sleeping of(Member member){
        return Sleeping.builder()
                .member(member)
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .email(member.getEmail())
                .name(member.getName())
                .studentId(member.getStudentId())
                .major(member.getMajor().toString())
                .phoneNumber(member.getPhoneNumber())
                .expiredAt(LocalDateTime.now().plusDays(60))
                .build();
    }

}
