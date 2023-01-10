package com.FlagHome.backend.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {
    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;
}
