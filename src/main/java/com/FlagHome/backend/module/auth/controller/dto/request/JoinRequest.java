package com.FlagHome.backend.module.auth.controller.dto.request;

import com.FlagHome.backend.module.auth.domain.JoinType;
import com.FlagHome.backend.module.member.domain.enums.Major;
import com.FlagHome.backend.global.annotation.EnumFormat;
import com.FlagHome.backend.global.annotation.PasswordFormat;
import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {
    @Schema(description = "아이디", example = "gmlwh124")
    @NotBlank
    private String loginId;

    @Schema(description = "비밀번호", example = "qwer1234!")
    @PasswordFormat
    private String password;

    @Schema(description = "이름", example = "문희조")
    @NotBlank
    private String name;

    @Schema(name = "닉네임", example = "john")
    @NotBlank
    private String nickname;

    @Schema(description = "이메일", example = "gmlwh124@suwon.ac.kr")
    @USWEmailFormat
    private String email;

    @Schema(description = "전공", example = "컴퓨터SW")
    @EnumFormat(enumClass = Major.class)
    private Major major;

    @Schema(description = "학번", example = "19017041")
    @NotBlank
    private String studentId;

    @Schema(description = "가입 구분", example = "NORMAL / CREW")
    @EnumFormat(enumClass = JoinType.class)
    private JoinType joinType;

    @Builder
    public JoinRequest(String loginId, String password, String name, String nickname,
                       String email, Major major, String studentId, JoinType joinType) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.major = major;
        this.studentId = studentId;
        this.joinType = joinType;
    }
}
