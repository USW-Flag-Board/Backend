package com.FlagHome.backend.domain.auth.controller.dto;

import com.FlagHome.backend.global.annotation.PasswordFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @PasswordFormat
    @Schema(description = "비밀번호", required = true, example = "qwer1234!")
    private String password;

    public LoginRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
