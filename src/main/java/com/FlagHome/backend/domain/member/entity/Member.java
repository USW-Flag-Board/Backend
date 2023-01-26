package com.FlagHome.backend.domain.member.entity;

import com.FlagHome.backend.domain.BaseEntity;
import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.member.Major;
import com.FlagHome.backend.domain.member.Role;
import com.FlagHome.backend.domain.member.dto.UpdateProfileRequest;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
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

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    public void updatePassword(String password) { this.password = password; }

    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        this.major = updateProfileRequest.getMajor();
        this.bio = updateProfileRequest.getBio();
        this.phoneNumber = updateProfileRequest.getPhoneNumber();
        this.studentId = updateProfileRequest.getStudentId();
    }

    public void updateLastLoginTime(LocalDateTime loginTime) {
        this.lastLoginTime = loginTime;
    }

    public static Member of(AuthInformation authInformation, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(authInformation.getLoginId())
                .password(passwordEncoder.encode(authInformation.getPassword()))
                .name(authInformation.getName())
                .email(authInformation.getEmail())
                .major(authInformation.getMajor())
                .studentId(authInformation.getStudentId())
                .bio(" ")
                .phoneNumber(" ")
                .profileImg("default")
                .role(Role.from(authInformation.getJoinType()))
                .status(Status.GENERAL)
                .build();
    }
}
