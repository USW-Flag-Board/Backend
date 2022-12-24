package com.FlagHome.backend.domain.member.entity;

import com.FlagHome.backend.domain.BaseEntity;
import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.auth.entity.AuthMember;
import com.FlagHome.backend.domain.member.Major;
import com.FlagHome.backend.domain.member.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "major")
    private Major major;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updatePassword(String password) { this.password = password; }

    public static Member of(AuthMember authMember, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(authMember.getLoginId())
                .password(passwordEncoder.encode(authMember.getPassword()))
                .name(authMember.getName())
                .email(authMember.getEmail())
                .major(authMember.getMajor())
                .studentId(authMember.getStudentId())
                .bio(" ")
                .phoneNumber(" ")
                .profileImg("default")
                .role(Role.ROLE_USER)
                .status(Status.GENERAL)
                .build();
    }
}
