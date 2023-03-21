package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.global.annotation.PasswordFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WithdrawRequest {
    @PasswordFormat
    @Schema(name = "현재 비밀번호", required = true, example = "qwer1234!")
    private String currentPassword;
}
