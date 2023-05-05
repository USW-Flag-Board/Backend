package com.FlagHome.backend.module.member.controller.dto.request;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindIdRequest {
    @Schema(name = "이름")
    @NotBlank
    private String name;

    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    @USWEmailFormat
    private String email;
}
