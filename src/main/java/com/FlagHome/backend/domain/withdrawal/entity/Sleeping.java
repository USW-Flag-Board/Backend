package com.FlagHome.backend.domain.withdrawal.entity;

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
    private String bio;

    @Column
    private String major;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "phone_number")
    private String phoneNumber;

    public static Sleeping of(Member member, PasswordEncoder passwordEncoder){
        return Sleeping.builder()
                .member(member)
                .loginId(passwordEncoder.encode(member.getLoginId()))
                .password(passwordEncoder.encode(member.getPassword()))
                .email(passwordEncoder.encode(member.getEmail()))
                .name(passwordEncoder.encode(member.getName()))
                .studentId(passwordEncoder.encode(member.getStudentId()))
                .major(passwordEncoder.encode(member.getMajor().toString()))
                .phoneNumber(passwordEncoder.encode(member.getPhoneNumber()))
                .expiredAt(LocalDateTime.now().plusDays(90))
                .build();
    }

}
