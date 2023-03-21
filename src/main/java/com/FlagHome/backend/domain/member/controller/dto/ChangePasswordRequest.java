package com.FlagHome.backend.domain.member.controller.dto;

import com.FlagHome.backend.global.annotation.PasswordFormat;
import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChangePasswordRequest {
    @USWEmailFormat
    @Schema(name = "이메일", required = true, example = "gmlwh124@suwon.ac.kr")
    private String email;

    @PasswordFormat
    @Schema(name = "새 비밀번호", required = true, example = "qwer1234!")
    private String newPassword;
}
