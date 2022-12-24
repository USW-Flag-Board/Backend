package com.FlagHome.backend.domain.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckEmailRequest {
    @ApiModelProperty(value = "이메일")
    private String email;
}
