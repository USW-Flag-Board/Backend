package com.FlagHome.backend.domain.auth.entity;

import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.auth.dto.JoinRequest;
import com.FlagHome.backend.domain.member.Major;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
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
    private String nickName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "student_id")
    private String studentId;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Major major;

    @Column(name = "join_type")
    @Enumerated(value = EnumType.STRING)
    private JoinType joinType;

    @Column
    private boolean isAuthorizedCrew;

    @Column
    private String certification;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    public static AuthInformation of(JoinRequest joinRequest, String certification) {
        return AuthInformation.builder()
                .loginId(joinRequest.getLoginId())
                .password(joinRequest.getPassword())
                .name(joinRequest.getName())
                .email(joinRequest.getEmail())
                .major(joinRequest.getMajor())
                .nickName(joinRequest.getNickName())
                .studentId(joinRequest.getStudentId())
                .phoneNumber(joinRequest.getPhoneNumber())
                .joinType(joinRequest.getJoinType())
                .isAuthorizedCrew(false)
                .certification(certification)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void authorized() {
        this.isAuthorizedCrew = true;
    }

    public void validateAuthTime() {
        final LocalDateTime expireAt = this.createdAt.plusMinutes(10);
        if (expireAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EXPIRED_AUTH_INFORMATION);
        }
    }
}
