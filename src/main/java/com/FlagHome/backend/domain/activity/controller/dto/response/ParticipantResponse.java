package com.FlagHome.backend.domain.activity.controller.dto.response;

import com.FlagHome.backend.domain.member.entity.enums.Major;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantResponse {
    private String name;
    private String loginId;
    private Major major;

    @QueryProjection
    public ParticipantResponse(String name, String loginId, Major major) {
        this.name = name;
        this.loginId = loginId;
        this.major = major;
    }
}
