package com.Flaground.backend.module.member.controller.dto.request;

import com.Flaground.backend.global.annotation.PasswordFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdatePasswordRequest {
    @Schema(description = "현재 비밀번호", example = "qwer1234!")
    @PasswordFormat
    private String currentPassword;

    @Schema(description = "새 비밀번호", example = "qwer1234!")
    @PasswordFormat
    private String newPassword;
}
