package com.FlagHome.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(description = "인증 번호", required = true, example = "123456")
    private String certification;
}
