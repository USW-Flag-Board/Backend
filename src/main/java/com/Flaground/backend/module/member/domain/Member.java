package com.Flaground.backend.module.member.domain;

import com.Flaground.backend.global.common.BaseEntity;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.member.domain.enums.MemberStatus;
import com.Flaground.backend.module.member.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

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

    @Embedded
    private Avatar avatar;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column
    private MemberStatus status;

    @Builder
    public Member(String loginId, String password, String email, String name, Avatar avatar, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.role = role;
        this.status = MemberStatus.NORMAL;
    }

    public void isAvailable() {
        if (this.status == MemberStatus.WITHDRAW || this.status == MemberStatus.BANNED) {
            throw new CustomException(ErrorCode.UNAVAILABLE_ACCOUNT);
        }
    }

    public void validateName(String name) {
        if (!StringUtils.equals(this.name, name)) {
            throw new CustomException(ErrorCode.EMAIL_NAME_NOT_MATCH);
        }
    }

    public void validateLoginId(String loginId) {
        if (!StringUtils.equals(this.loginId, loginId)) {
            throw new CustomException(ErrorCode.EMAIL_ID_NOT_MATCH);
        }
    }

    public void validatePassword(String password, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(password, this.password)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    public void validateSamePassword(String password, PasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(password, this.password)) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void changeProfileImage(String profileImage) {
        this.avatar.changeProfileImage(profileImage);
    }

    public String resetProfileImage() {
        return avatar.resetProfileImage();
    }

    public void updateLoginTime() {
        super.updateModifiedDate();
    }

    public void reactivate(String loginId, String password, String email, String name) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.status = MemberStatus.NORMAL;
    }

    public void deactivate() {
        this.status = MemberStatus.SLEEPING;
        this.loginId = null;
        this.password = null;
        this.email = null;
        this.name = null;
        this.avatar.deactivate();
    }

    public void withdraw() {
        this.status = MemberStatus.WITHDRAW;
    }
}
