package com.FlagHome.backend.v1.member.dto;

import com.FlagHome.backend.v1.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "자기소개")
    private String bio;

    @ApiModelProperty(value = "핸드폰 번호")
    private String phoneNumber;
}
