package com.Flaground.backend.module.auth.domain;

import com.Flaground.backend.module.member.domain.JoinMember;
import com.Flaground.backend.module.member.domain.enums.Major;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "auth_information")
public class AuthInformation {
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

    @Column
    private String nickname;

    @Column(name = "student_id")
    private String studentId;

    @Enumerated(value = EnumType.STRING)
    @Column
    private Major major;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "join_type")
    private JoinType joinType;

    @Column
    private String certification;

    @Column(name = "authorized")
    private boolean isAuthorizedCrew;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public AuthInformation(String loginId, String password, String name, String email, String nickname,
                           String studentId, Major major, JoinType joinType, String certification) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.studentId = studentId;
        this.major = major;
        this.joinType = joinType;
        this.isAuthorizedCrew = false;
        this.certification = certification;
        this.createdAt = LocalDateTime.now();
    }

    public void authorized() {
        this.isAuthorizedCrew = true;
    }

    public boolean isCrewJoin() {
        return this.joinType == JoinType.CREW;
    }

    public void validateAuthTime() {
        if (createdAt.plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_AUTHENTICATION_TIME);
        }
    }

    public void validateCertification(String certification) {
        if (!StringUtils.equals(certification, this.certification)) {
            throw new CustomException(ErrorCode.CERTIFICATION_NOT_MATCH);
        }
    }

    public JoinMember toJoinMember() {
        return JoinMember.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .name(name)
                .nickname(nickname)
                .studentId(studentId)
                .major(major)
                .role(joinType.toRole())
                .build();
    }
}
