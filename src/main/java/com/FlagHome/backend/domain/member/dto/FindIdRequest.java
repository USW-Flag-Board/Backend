package com.FlagHome.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindIdRequest {
    @Parameter(description = "이름", required = true, example = "문희조")
    private String name;

    @Parameter(description = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;
}
