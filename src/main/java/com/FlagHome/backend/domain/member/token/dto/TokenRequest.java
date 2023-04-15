package com.FlagHome.backend.domain.member.token.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenRequest {
    @Schema(name = "Access 토큰")
    @NotBlank
    private String accessToken;
    
    @Schema(name = "Refresh 토큰", description = "재발급 시 추가적인 인스턴스를 생성하지 않으려고 받음")
    @NotBlank
    private String refreshToken;

    @Builder
    public TokenRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
