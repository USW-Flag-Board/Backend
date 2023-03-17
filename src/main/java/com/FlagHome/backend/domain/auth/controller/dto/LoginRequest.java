package com.FlagHome.backend.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @Schema(description = "비밀번호", required = true, example = "qwer1234!")
    private String password;
}
