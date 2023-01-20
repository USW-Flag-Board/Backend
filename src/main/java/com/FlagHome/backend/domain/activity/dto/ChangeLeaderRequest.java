package com.FlagHome.backend.domain.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLeaderRequest {
    @Schema(name = "활동 고유번호", required = true)
    private long id;

    @Schema(name = "아아디", required = true, description = "로그인 시 사용되는 아이디")
    private String loginId;
}
