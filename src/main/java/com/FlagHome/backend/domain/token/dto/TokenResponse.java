package com.FlagHome.backend.domain.token.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    @Schema(name = "허가 유형", example = "Bearer")
    private String grantType;

    @Schema(name = "Access Token")
    private String accessToken;

    @Schema(name = "Refresh Token")
    private String refreshToken;

    @Schema(name = "만료시간", example = "1659476381416")
    private Long accessTokenExpiresIn;
}
