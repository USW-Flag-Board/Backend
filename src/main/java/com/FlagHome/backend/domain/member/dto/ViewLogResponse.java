package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class ViewLogResponse {

    @Schema(name = "아이디", example = "gmlwh124")
    private String loginId;

    @Schema(name = "이름", example = "김민정")
    private String name;

    @Schema(name = "마지막 로그인 시간")
    private LocalDateTime lastLoginTime;

    @Builder
    @QueryProjection
    public ViewLogResponse(String loginId, String name, LocalDateTime lastLoginTime) {
        this.loginId = loginId;
        this.name = name;
        this.lastLoginTime = lastLoginTime;
    }
}
