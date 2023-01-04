package com.FlagHome.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReissuePasswordRequest {
    @Parameter(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @Parameter(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;
}
