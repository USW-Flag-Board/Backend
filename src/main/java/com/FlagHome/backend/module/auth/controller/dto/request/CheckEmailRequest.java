package com.FlagHome.backend.module.auth.controller.dto.request;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckEmailRequest {
    @Schema(description = "이메일", example = "gmlwh124@suwon.ac.kr")
    @USWEmailFormat
    private String email;
}
