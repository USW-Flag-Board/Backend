package com.FlagHome.backend.domain.auth.dto;

import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.auth.entity.AuthMember;
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
public class SignUpResponse {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(description = "가입 구분", required = true, example = "일반 / 동아리")
    private JoinType joinType;

    public static SignUpResponse from(AuthMember authMember) {
        return new SignUpResponse(authMember.getLoginId(), authMember.getEmail(), authMember.getJoinType());
    }
}
