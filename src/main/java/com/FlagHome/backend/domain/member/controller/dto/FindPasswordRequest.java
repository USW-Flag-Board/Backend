package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FindPasswordRequest {
    @Schema(description = "아이디", example = "gmlwh124")
    private String loginId;

    @USWEmailFormat
    @Schema(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;
}
