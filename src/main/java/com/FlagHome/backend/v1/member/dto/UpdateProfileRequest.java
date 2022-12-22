package com.FlagHome.backend.v1.member.dto;

import com.FlagHome.backend.v1.member.Major;
import com.FlagHome.backend.v1.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.BasicAuthDefinition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
