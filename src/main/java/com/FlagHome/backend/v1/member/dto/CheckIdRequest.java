package com.FlagHome.backend.v1.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckIdRequest {
    @ApiModelProperty(value = "로그인 아이디")
    private String loginId;
}
