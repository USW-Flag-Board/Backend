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
    @Schema(name = "신청 고유번호")
    private long id;

    @Schema(name = "아이디", description = "로그인 시 사용되는 아이디", example = "gmlwh124")
    private String loginId;

    @Schema(name = "이름")
    private String name;

    @Schema(name = "전공", example = "컴퓨터SW")
    private Major major;

    @Builder
    @QueryProjection
    public ActivityApplyResponse(long id, String loginId, String name, Major major) {
        this.id = id;
        this.loginId = loginId;
        this.name = name;
        this.major = major;
    }
}
