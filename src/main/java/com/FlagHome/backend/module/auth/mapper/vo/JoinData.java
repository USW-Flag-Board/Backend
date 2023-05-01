package com.FlagHome.backend.module.auth.mapper.vo;

import com.FlagHome.backend.module.auth.controller.dto.request.JoinRequest;
import com.FlagHome.backend.module.auth.domain.JoinType;
import com.FlagHome.backend.module.member.domain.enums.Major;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinData {
    private String loginId;
    private String password;
    private String name;
    private String nickname;
    private String email;
    private Major major;
    private String studentId;
    private JoinType joinType;

    @Builder
    public JoinData(String loginId, String password, String name, String nickname, String email,
                    Major major, String studentId, JoinType joinType) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.major = major;
        this.studentId = studentId;
        this.joinType = joinType;
    }

    public static JoinData from(JoinRequest joinRequest) {
        return JoinData.builder()
                .loginId(joinRequest.getLoginId())
                .password(joinRequest.getPassword())
                .name(joinRequest.getName())
                .nickname(joinRequest.getNickname())
                .email(joinRequest.getEmail())
                .major(joinRequest.getMajor())
                .studentId(joinRequest.getStudentId())
                .joinType(joinRequest.getJoinType())
                .build();
    }
}
