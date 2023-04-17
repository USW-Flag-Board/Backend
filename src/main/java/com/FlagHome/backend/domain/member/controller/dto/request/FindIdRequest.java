package com.FlagHome.backend.domain.member.controller.dto.request;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindIdRequest {
    @Schema(name = "이름", required = true)
    @NotBlank
    private String name;

    @USWEmailFormat
    @Schema(name = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;
}
