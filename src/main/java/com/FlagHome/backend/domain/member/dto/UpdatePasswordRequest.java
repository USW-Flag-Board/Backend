package com.FlagHome.backend.domain.member.dto;

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
    @Schema(description = "현재 비밀번호", required = true, example = "qwer1234!")
    private String currentPassword;

    @Schema(description = "새 비밀번호", required = true, example = "qwer1234!")
    private String newPassword;
}
