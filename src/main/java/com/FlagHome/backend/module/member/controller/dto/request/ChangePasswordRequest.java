package com.FlagHome.backend.module.member.controller.dto.request;

import com.FlagHome.backend.global.annotation.PasswordFormat;
import com.FlagHome.backend.global.annotation.USWEmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChangePasswordRequest {
    @Schema(name = "이메일", example = "gmlwh124@suwon.ac.kr")
    @USWEmailFormat
    private String email;

    @Schema(name = "새 비밀번호", example = "qwer1234!")
    @PasswordFormat
    private String newPassword;
}
