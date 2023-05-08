package com.Flaground.backend.module.member.domain;

import com.Flaground.backend.module.member.domain.enums.Major;
import com.Flaground.backend.module.member.domain.enums.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinMember {
    private String loginId;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String studentId;
    private Major major;
    private Role role;

    @Builder
    public JoinMember(String loginId, String password, String email, String name,
                      String nickname, String studentId, Major major, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.studentId = studentId;
        this.major = major;
        this.role = role;
    }

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .avatar(Avatar.of(nickname, studentId, major))
                .role(role)
                .build();
    }
}
