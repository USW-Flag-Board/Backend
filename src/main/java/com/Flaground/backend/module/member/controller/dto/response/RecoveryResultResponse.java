package com.Flaground.backend.module.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecoveryResultResponse {
    @Schema(name = "로그인 아이디")
    private String loginId;

    @Schema(name = "이메일")
    private String email;

    @Builder
    public RecoveryResultResponse(String loginId, String email) {
        this.loginId = loginId;
        this.email = email;
    }
}
