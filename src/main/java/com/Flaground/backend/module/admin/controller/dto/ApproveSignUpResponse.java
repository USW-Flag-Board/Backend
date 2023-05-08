package com.Flaground.backend.module.admin.controller.dto;

import com.Flaground.backend.module.member.domain.enums.Major;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApproveSignUpResponse {
    private long id;
    private String name;
    private String email;
    private Major major;

    @Builder
    @QueryProjection
    public ApproveSignUpResponse(long id, String name, String email, Major major) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.major = major;
    }
}
