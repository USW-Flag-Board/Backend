package com.FlagHome.backend.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckLoginIdRequest {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;
}
