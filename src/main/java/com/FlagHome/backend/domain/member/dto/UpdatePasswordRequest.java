package com.FlagHome.backend.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {
    @ApiModelProperty(value = "현재 비밀번호")
    private String currentPassword;

    @ApiModelProperty(value = "새 비밀번호")
    private String newPassword;
}
