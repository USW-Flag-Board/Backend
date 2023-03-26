package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.global.common.Status;
import com.FlagHome.backend.domain.auth.AuthInformation;
import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Member(String loginId, String password, String email, String name, String studentId, Major major, String phoneNumber, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.studentId = studentId;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = Status.GENERAL;
        this.lastLoginTime = LocalDateTime.now();
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void renewLoginTime() {
        this.lastLoginTime = LocalDateTime.now();
    }

    public void sleepToMember(Sleeping sleeping) {
        this.loginId = sleeping.getLoginId();
        this.password = sleeping.getPassword();
        this.email = sleeping.getEmail();
        this.name = sleeping.getName();
        this.studentId = sleeping.getStudentId();
        this.major = sleeping.getMember().getMajor();
        this.phoneNumber = sleeping.getPhoneNumber();
        this.status = Status.GENERAL;
    }

    public void changeToSleep() {
        this.status = Status.SLEEPING;
        this.loginId = null;
        this.password = null;
        this.email = null;
        this.name = null;
        this.studentId = null;
        this.major = null;
        this.phoneNumber = null;
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
                .build();
    }
}
