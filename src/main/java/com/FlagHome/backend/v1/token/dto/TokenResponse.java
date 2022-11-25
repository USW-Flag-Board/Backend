package com.FlagHome.backend.v1.token.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    @ApiModelProperty(example = "Bearer")
    private String grantType;

    @ApiModelProperty(example = "")
    private String accessToken;

    @ApiModelProperty(example = "")
    private String refreshToken;

    @ApiModelProperty(example = "1659476381416")
    private Long accessTokenExpiresIn;
}
