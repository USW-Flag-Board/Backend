package com.FlagHome.backend.domain.member.entity;

import com.FlagHome.backend.domain.BaseEntity;
import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.member.Major;
import com.FlagHome.backend.domain.member.Role;
import com.FlagHome.backend.domain.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.sleeping.repository.SleepingRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "major")
    private Major major;

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

    public void updateLastLoginTime(LocalDateTime loginTime) {
        this.lastLoginTime = loginTime;
    }

    public void sleepToMember(Sleeping sleeping) {
        this.loginId = sleeping.getLoginId();
        this.password = sleeping.getPassword();
        this.email = sleeping.getEmail();
        this.name = sleeping.getName();
        this.studentId = sleeping.getStudentId();
        this.major = sleeping.getMember().getMajor();
        this.phoneNumber = sleeping.getPhoneNumber();
    }

    public static Member of(AuthInformation authInformation, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(authInformation.getLoginId())
                .password(passwordEncoder.encode(authInformation.getPassword()))
                .name(authInformation.getName())
                .email(authInformation.getEmail())
                .major(authInformation.getMajor())
                .studentId(authInformation.getStudentId())
                .phoneNumber(authInformation.getPhoneNumber())
                .role(Role.from(authInformation.getJoinType()))
                .status(Status.GENERAL)
                .lastLoginTime(null)
                .build();
    }
}
