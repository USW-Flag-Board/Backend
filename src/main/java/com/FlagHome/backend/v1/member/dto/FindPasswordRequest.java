package com.FlagHome.backend.v1.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindPasswordRequest {
    @ApiModelProperty(value = "로그인 아이디")
    private String loginId;

    @ApiModelProperty(value = "이메일")
    private String email;
}
