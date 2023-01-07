package com.FlagHome.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @Parameter(description = "현재 비밀번호", required = true, example = "qwer1234!")
    private String currentPassword;

    @Parameter(description = "새 비밀번호", required = true, example = "qwer1234!")
    private String newPassword;
}
