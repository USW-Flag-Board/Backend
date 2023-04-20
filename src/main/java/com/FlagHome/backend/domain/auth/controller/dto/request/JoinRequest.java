package com.FlagHome.backend.domain.auth.controller.dto.request;

import com.FlagHome.backend.domain.auth.entity.JoinType;
import com.FlagHome.backend.domain.member.entity.enums.Major;
import com.FlagHome.backend.global.annotation.PasswordFormat;
import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JoinRequest {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    @NotBlank
    private String loginId;

    @PasswordFormat
    @Schema(description = "비밀번호", required = true, example = "qwer1234!")
    private String password;

    @Schema(description = "이름", required = true, example = "문희조")
    @NotBlank
    private String name;

    @Schema(name = "닉네임", required = true, example = "john")
    @NotBlank
    private String nickname;

    @USWEmailFormat
    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(description = "전공", required = true, example = "컴퓨터SW")
    @NotNull
    private Major major;

    @Schema(description = "학번", required = true, example = "19017041")
    @NotBlank
    private String studentId;

    @Schema(description = "가입 구분", required = true, example = "일반 / 동아리")
    @NotNull
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
