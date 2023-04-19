package com.FlagHome.backend.domain.auth;

import com.FlagHome.backend.domain.auth.controller.dto.request.JoinRequest;
import com.FlagHome.backend.domain.member.entity.enums.Major;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.*;
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

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "student_id")
    private String studentId;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Major major;

    @Column(name = "join_type")
    @Enumerated(value = EnumType.STRING)
    private JoinType joinType;

    @Column
    private String certification;

    @Column(name = "authorized")
    private boolean isAuthorizedCrew;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public AuthInformation(String loginId, String password, String name, String email, String nickName,
                           String studentId, Major major, JoinType joinType, String certification) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.nickName = nickName;
        this.studentId = studentId;
        this.major = major;
        this.joinType = joinType;
        this.isAuthorizedCrew = false;
        this.certification = certification;
        this.createdAt = LocalDateTime.now();
    }

    public static AuthInformation of(JoinRequest joinRequest, String certification) {
        return AuthInformation.builder()
                .loginId(joinRequest.getLoginId())
                .password(joinRequest.getPassword())
                .name(joinRequest.getName())
                .email(joinRequest.getEmail())
                .major(joinRequest.getMajor())
                .nickName(joinRequest.getNickName())
                .studentId(joinRequest.getStudentId())
                .joinType(joinRequest.getJoinType())
                .certification(certification)
                .build();
    }

    public void authorized() {
        this.isAuthorizedCrew = true;
    }

    public boolean isCrewJoin() {
        return this.joinType == JoinType.CREW;
    }

    public void validateAuthTime() {
        final LocalDateTime expireAt = this.createdAt.plusMinutes(5);
        if (expireAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_AUTHENTICATION_TIME);
        }
    }

    public void validateCertification(String certification) {
        if (!StringUtils.equals(certification, this.certification)) {
            throw new CustomException(ErrorCode.CERTIFICATION_NOT_MATCH);
        }
    }

    // test용 : 삭제 예정
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
