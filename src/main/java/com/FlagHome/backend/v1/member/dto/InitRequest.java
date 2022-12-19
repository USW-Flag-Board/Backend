package com.FlagHome.backend.v1.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitRequest {
    @ApiModelProperty(example = "gmlwh124@naver.com")
    private String email;

    @ApiModelProperty(example = "01040380540")
    private String phoneNumber;

    @ApiModelProperty(value = "자기소개")
    private String bio;
}
