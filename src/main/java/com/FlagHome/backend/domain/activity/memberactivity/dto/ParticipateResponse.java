package com.FlagHome.backend.domain.activity.memberactivity.dto;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.entity.enums.Semester;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipateResponse {
    @Schema(name = "활동 식별번호")
    private long id;

    @Schema(name = "활동 이름")
    private String name;

    @Schema(name = "활동 연도")
    private int year;

    @Schema(name = "활동 시즌")
    private Semester semester;

    @Schema(name = "활동 상태")
    private ActivityStatus activityStatus;

    @Builder
    @QueryProjection
    public ParticipateResponse(long id, String name, int year,
                               Semester semester, ActivityStatus activityStatus) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.activityStatus = activityStatus;
    }
}
