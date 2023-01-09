package com.FlagHome.backend.domain.withdrawal.entity;

import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.member.Major;
import com.FlagHome.backend.domain.member.Role;
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
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column
    private String role;

    public static Withdrawal of(Member member, PasswordEncoder passwordEncoder){
        return Withdrawal.builder()
                .loginId(passwordEncoder.encode(member.getLoginId()))
                .password(passwordEncoder.encode(member.getPassword()))
                .email(passwordEncoder.encode(member.getEmail()))
                .name(passwordEncoder.encode(member.getName()))
                .studentId(passwordEncoder.encode(member.getStudentId()))
                .bio(passwordEncoder.encode(member.getBio()))
                .major(passwordEncoder.encode(member.getMajor().toString()))
                .profileImg(passwordEncoder.encode(member.getProfileImg()))
                .phoneNumber(passwordEncoder.encode(member.getPhoneNumber()))
                .role(passwordEncoder.encode(member.getRole().toString()))
                .expiredAt(LocalDateTime.now().plusDays(90))
                .build();
    }

}
