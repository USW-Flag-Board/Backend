package com.FlagHome.backend.module.member.domain;

import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.module.member.domain.enums.MemberStatus;
import com.FlagHome.backend.module.member.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
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

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
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