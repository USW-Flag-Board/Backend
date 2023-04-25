package com.FlagHome.backend.domain.auth.controller.dto.request;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {
    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    @USWEmailFormat
    private String email;

    @Schema(description = "인증 번호", required = true, example = "123456")
    @NotBlank
    private String certification;

    @Builder
    public SignUpRequest(String email, String certification) {
        this.email = email;
        this.certification = certification;
    }
}
