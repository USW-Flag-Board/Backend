package com.FlagHome.backend.domain.auth.dto;

import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.member.Major;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @Schema(description = "비밀번호", required = true, example = "qwer1234!")
    private String password;

    @Schema(description = "이름", required = true, example = "문희조")
    private String name;

    @Schema(name = "닉네임", required = true, example = "john")
    private String nickName;

    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(description = "전공", required = true, example = "컴퓨터SW")
    private Major major;

    @Schema(description = "학번", required = true, example = "19017041")
    private String studentId;

    @Schema(name = "핸드폰 번호", required = true, example = "01040380540")
    private String phoneNumber;

    @Schema(description = "가입 구분", required = true, example = "일반 / 동아리")
    private JoinType joinType;
}
