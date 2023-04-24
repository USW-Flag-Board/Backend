package com.FlagHome.backend.domain.member.controller.dto.request;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FindPasswordRequest {
    @Schema(description = "아이디", example = "gmlwh124")
    @NotBlank
    private String loginId;

    @Schema(description = "이메일", example = "gmlwh124@suwon.ac.kr")
    @USWEmailFormat
    private String email;
}
