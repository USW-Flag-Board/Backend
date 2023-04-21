package com.FlagHome.backend.domain.activity.controller.dto.response;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.Semester;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityResponse {
    private Long id;
    private String name;
    private String leader;
    private ActivityType activityType;
    private ActivityStatus status;
    private String semester;

    @QueryProjection
    public ActivityResponse(Long id, String name, String leader, ActivityType activityType,
                            ActivityStatus status, Semester semester) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.activityType = activityType;
        this.status = status;
        this.semester = semester.getSemester();
    }
}
