package com.FlagHome.backend.v1.member.entity;

import com.FlagHome.backend.v1.BaseEntity;
import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.member.Major;
import com.FlagHome.backend.v1.member.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public Member(Long id, String loginId, String password, String email, String name,
                  String studentId, Major major, Role role, Status status) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.studentId = studentId;
        this.major = major;
        this.role = role;
        this.status = status;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void updatePassword(String password) { this.password = password; }
}
