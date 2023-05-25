package com.Flaground.backend.module.auth.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {
    @Schema(description = "아이디", example = "gmlwh124")
    @NotBlank
    private String loginId;

    @Schema(description = "비밀번호", example = "qwer1234!")
    @NotBlank
    private String password;

    @Builder
    public LoginRequest(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
