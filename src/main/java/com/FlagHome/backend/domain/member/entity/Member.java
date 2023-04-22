package com.FlagHome.backend.domain.member.entity;

import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.member.entity.enums.MemberStatus;
import com.FlagHome.backend.domain.member.entity.enums.Role;
import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import com.FlagHome.backend.global.common.BaseEntity;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
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

    public static Member of(AuthInformation authInformation, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(authInformation.getLoginId())
                .password(passwordEncoder.encode(authInformation.getPassword()))
                .name(authInformation.getName())
                .email(authInformation.getEmail())
                .avatar(Avatar.of(authInformation.getNickname(), authInformation.getStudentId(), authInformation.getMajor()))
                .role(Role.from(authInformation.getJoinType()))
                .build();
    }

    public void withdraw() {
        this.status = MemberStatus.WITHDRAW;
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

    public void reactivate(Sleeping sleeping) {
        this.loginId = sleeping.getLoginId();
        this.password = sleeping.getPassword();
        this.email = sleeping.getEmail();
        this.name = sleeping.getName();
        this.status = MemberStatus.NORMAL;
    }

    public void deactivate() {
        this.status = MemberStatus.SLEEPING;
        this.loginId = null;
        this.password = null;
        this.email = null;
        this.name = null;
    }
}
