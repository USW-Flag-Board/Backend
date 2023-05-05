package com.FlagHome.backend.module.member.controller.dto.request;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthenticationRequest {
    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    @USWEmailFormat
    private String email;

    @Schema(name = "인증 번호", example = "123456")
    @NotBlank
    private String certification;
}
