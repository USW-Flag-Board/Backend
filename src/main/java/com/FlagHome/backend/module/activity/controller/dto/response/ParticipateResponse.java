package com.FlagHome.backend.module.activity.controller.dto.response;

import com.FlagHome.backend.module.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.module.activity.entity.enums.Semester;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipateResponse {
    private Long id;

    private String name;

    private Integer year;

    private Semester semester;

    private ActivityStatus activityStatus;

    @QueryProjection
    public ParticipateResponse(Long id, String name, Integer year,
                               Semester semester, ActivityStatus activityStatus) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.activityStatus = activityStatus;
    }
}
