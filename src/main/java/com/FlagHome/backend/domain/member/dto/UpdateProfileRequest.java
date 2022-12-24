package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.member.Major;
import io.swagger.annotations.ApiModelProperty;

public class UpdateProfileRequest {

    @ApiModelProperty(value = "자기소개")
    private String bio;

    @ApiModelProperty(value = "핸드폰 번호")
    private String phoneNumber;

    @ApiModelProperty(value = "전공")
    private Major major;

    @ApiModelProperty(value = "학번")
    private String studentId;
}
