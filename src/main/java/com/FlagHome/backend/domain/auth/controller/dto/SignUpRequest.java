package com.FlagHome.backend.domain.auth.controller.dto;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    @USWEmailFormat
    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(description = "인증 번호", required = true, example = "123456")
    private String certification;

    public SignUpRequest(String email, String certification) {
        this.email = email;
        this.certification = certification;
    }
}
