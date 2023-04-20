package com.FlagHome.backend.domain.auth.controller.dto.response;

import com.FlagHome.backend.domain.auth.entity.JoinType;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpResponse {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(description = "가입 구분", required = true, example = "일반 / 동아리")
    private JoinType joinType;

    @Builder
    public SignUpResponse(String loginId, String email, JoinType joinType) {
        this.loginId = loginId;
        this.email = email;
        this.joinType = joinType;
    }

    public static SignUpResponse from(AuthInformation authInformation) {
        return SignUpResponse.builder()
                .loginId(authInformation.getLoginId())
                .email(authInformation.getEmail())
                .joinType(authInformation.getJoinType())
                .build();
    }
}
