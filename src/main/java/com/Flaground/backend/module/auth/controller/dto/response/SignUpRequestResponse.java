package com.Flaground.backend.module.auth.controller.dto.response;

import com.Flaground.backend.module.member.domain.enums.Major;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestResponse {
    private Long id;
    private String name;
    private String email;
    private Major major;

    @Builder
    @QueryProjection
    public SignUpRequestResponse(Long id, String name, String email, Major major) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.major = major;
    }
}
