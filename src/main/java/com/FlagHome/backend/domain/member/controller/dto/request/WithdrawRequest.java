package com.FlagHome.backend.domain.member.controller.dto.request;

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
    @Schema(name = "현재 비밀번호", example = "qwer1234!")
    @PasswordFormat
    private String currentPassword;
}
