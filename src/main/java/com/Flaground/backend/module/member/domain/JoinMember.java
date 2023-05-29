package com.Flaground.backend.module.member.domain;

import com.Flaground.backend.module.member.domain.enums.Major;
import com.Flaground.backend.module.member.domain.enums.MemberStatus;
import com.Flaground.backend.module.member.domain.enums.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JoinMember {
    private String loginId;
    private String password;
    private String email;
    private String name;
    private String nickname;
    private String studentId;
    private Major major;
    private Role role;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .avatar(Avatar.of(nickname, studentId, major))
                .issueRecord(new IssueRecord())
                .role(role)
                .status(MemberStatus.NORMAL)
                .build();
    }
}
