package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.global.annotation.PasswordFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @PasswordFormat
    @Schema(description = "현재 비밀번호", required = true, example = "qwer1234!")
    private String currentPassword;

    @PasswordFormat
    @Schema(description = "새 비밀번호", required = true, example = "qwer1234!")
    private String newPassword;
}
