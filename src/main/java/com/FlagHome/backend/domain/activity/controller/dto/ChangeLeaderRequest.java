package com.FlagHome.backend.domain.activity.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangeLeaderRequest {
    @Schema(name = "아이디", description = "로그인 시에 사용되는 아이디", required = true)
    private String loginId;

    @Builder
    public ChangeLeaderRequest(String loginId) {
        this.loginId = loginId;
    }
}
