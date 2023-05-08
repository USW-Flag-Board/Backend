package com.Flaground.backend.module.auth.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CheckLoginIdRequest {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    @NotBlank
    private String loginId;
}
