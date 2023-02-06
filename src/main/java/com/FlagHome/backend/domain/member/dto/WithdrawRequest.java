package com.FlagHome.backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest {
    @Schema(name = "현재 비밀번호", required = true, example = "qwer1234!")
    private String currentPassword;
}
