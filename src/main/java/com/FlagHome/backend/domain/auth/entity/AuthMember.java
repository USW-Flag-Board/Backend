package com.FlagHome.backend.domain.auth.entity;

import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.auth.dto.JoinRequest;
import com.FlagHome.backend.domain.member.Major;
import com.FlagHome.backend.domain.member.Role;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="login_id")
    private String loginId;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String email;

    @Column(name = "student_id")
    private String studentId;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Major major;

    @Column(name = "join_type")
    @Enumerated(value = EnumType.STRING)
    private JoinType joinType;

    @Column
    private boolean isAuthorizedClubMember;

    @Column
    private String certification;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    public static AuthMember of(JoinRequest joinRequest, String certification) {
        return AuthMember.builder()
                .loginId(joinRequest.getLoginId())
                .password(joinRequest.getPassword())
                .name(joinRequest.getName())
                .email(joinRequest.getEmail())
                .major(joinRequest.getMajor())
                .studentId(joinRequest.getStudentId())
                .joinType(joinRequest.getJoinType())
                .isAuthorizedClubMember(false)
                .certification(certification)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateAuthorizedTrue() {
        this.isAuthorizedClubMember = true;
    }
}
