package com.FlagHome.backend.domain.member.entity;

import com.FlagHome.backend.domain.auth.AuthInformation;
import com.FlagHome.backend.domain.member.entity.enums.MemberStatus;
import com.FlagHome.backend.domain.member.entity.enums.Role;
import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import com.FlagHome.backend.global.common.BaseEntity;
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
        Avatar avatar = Avatar.of(authInformation.getNickName(), authInformation.getStudentId(), authInformation.getMajor());
        String password = passwordEncoder.encode(authInformation.getPassword());
        Role role = Role.from(authInformation.getJoinType());

        return Member.builder()
                .loginId(authInformation.getLoginId())
                .password(password)
                .name(authInformation.getName())
                .email(authInformation.getEmail())
                .avatar(avatar)
                .role(role)
                .build();
    }

    public void withdraw() {
        this.status = MemberStatus.WITHDRAW;
    }

    public boolean isNotAvailable() {
        return this.status == MemberStatus.WITHDRAW || this.status == MemberStatus.BANNED;
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void renewLoginTime() {
        super.renewLoginTime();
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
