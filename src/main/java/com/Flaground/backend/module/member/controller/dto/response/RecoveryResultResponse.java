package com.Flaground.backend.module.member.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RecoveryResultResponse {
    @Schema(name = "로그인 아이디")
    private String loginId;

    @Schema(name = "이메일")
    private String email;

    public static RecoveryResultResponse of(String loginId, String email) {
        return RecoveryResultResponse.builder()
                .loginId(loginId)
                .email(email)
                .build();
    }
}
