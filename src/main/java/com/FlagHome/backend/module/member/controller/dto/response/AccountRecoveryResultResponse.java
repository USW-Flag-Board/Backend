package com.FlagHome.backend.module.member.controller.dto.response;

import com.FlagHome.backend.module.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountRecoveryResultResponse {
    @Schema(name = "로그인 아이디")
    private String loginId;

    @Schema(name = "이메일")
    private String email;

    @Builder
    public AccountRecoveryResultResponse(String loginId, String email) {
        this.loginId = loginId;
        this.email = email;
    }

    public static AccountRecoveryResultResponse from(Member member) {
        return AccountRecoveryResultResponse.builder()
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .build();
    }
}
