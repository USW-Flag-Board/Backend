package com.FlagHome.backend.v1.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest {
    @ApiModelProperty(value = "비밀번호")
    private String password;
}
