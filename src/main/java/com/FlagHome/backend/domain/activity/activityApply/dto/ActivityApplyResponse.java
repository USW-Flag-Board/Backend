package com.FlagHome.backend.domain.activity.activityApply.dto;

import com.FlagHome.backend.domain.member.Major;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ActivityApplyResponse {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @Schema(description = "이름", required = true, example = "문희조")
    private String name;

    @Schema(description = "전공", required = true, example = "컴퓨터SW")
    private Major major;

    @Builder
    @QueryProjection
    public ActivityApplyResponse(String loginId, String name, Major major) {
        this.loginId = loginId;
        this.name = name;
        this.major = major;
    }
}
