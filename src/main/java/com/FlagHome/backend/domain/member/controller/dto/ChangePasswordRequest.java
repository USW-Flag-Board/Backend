package com.FlagHome.backend.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @Schema(name = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(name = "새 비밀번호", required = true, example = "qwer1234!")
    private String newPassword;
}
