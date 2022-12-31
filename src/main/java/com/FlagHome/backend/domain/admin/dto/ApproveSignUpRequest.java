package com.FlagHome.backend.domain.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApproveSignUpRequest {
    @ApiModelProperty(value = "인증 정보 ID")
    private long id;
}
