package com.Flaground.backend.module.member.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginLogResponse {
    private Long id;
    private String name;
    private LocalDateTime lastLoginTime;

    @Builder
    @QueryProjection
    public LoginLogResponse(Long id, String name, LocalDateTime lastLoginTime) {
        this.id = id;
        this.name = name;
        this.lastLoginTime = lastLoginTime;
    }
}
