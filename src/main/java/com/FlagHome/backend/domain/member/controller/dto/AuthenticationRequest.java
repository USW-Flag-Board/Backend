package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthenticationRequest {
    @USWEmailFormat
    @Schema(name = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @Schema(name = "인증 번호", required = true)
    private String certification;
}
