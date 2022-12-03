package com.FlagHome.backend.v1.member.entity;

import com.FlagHome.backend.v1.BaseEntity;
import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.member.Major;
import com.FlagHome.backend.v1.member.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
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
}
