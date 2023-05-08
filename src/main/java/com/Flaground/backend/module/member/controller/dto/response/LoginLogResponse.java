package com.Flaground.backend.module.member.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginLogResponse {

    @Schema(name = "아이디")
    private Long id;

    @Schema(name = "이름", example = "김민정")
    private String name;

    @Schema(name = "마지막 로그인 시간")
    private LocalDateTime lastLoginTime;

    @Builder
    @QueryProjection
    public LoginLogResponse(Long id, String name, LocalDateTime lastLoginTime) {
        this.id = id;
        this.name = name;
        this.lastLoginTime = lastLoginTime;
    }
}
