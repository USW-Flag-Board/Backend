package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindIdRequest {
    @Schema(name = "이름", required = true)
    private String name;

    @USWEmailFormat
    @Schema(name = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;
}
