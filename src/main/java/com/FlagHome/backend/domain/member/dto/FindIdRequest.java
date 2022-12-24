package com.FlagHome.backend.domain.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindIdRequest {
    @ApiModelProperty(value = "이름")
    private String name;
    
    @ApiModelProperty(value = "이메일")
    private String email;
}
