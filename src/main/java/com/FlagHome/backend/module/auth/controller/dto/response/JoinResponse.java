package com.FlagHome.backend.module.auth.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class JoinResponse {
    @Schema(description = "이메일", example = "gmlwh124@suwon.ac.kr")
    private String email;

    public static JoinResponse from(String email) {
        return new JoinResponse(email);
    }
}
