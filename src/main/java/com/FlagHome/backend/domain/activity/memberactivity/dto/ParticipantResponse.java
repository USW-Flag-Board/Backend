package com.FlagHome.backend.domain.activity.memberactivity.dto;

import com.FlagHome.backend.domain.member.Major;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParticipantResponse {
    private String name;
    private String loginId;
    private Major major;

    @Builder
    @QueryProjection
    public ParticipantResponse(String name, String loginId, Major major) {
        this.name = name;
        this.loginId = loginId;
        this.major = major;
    }
}
