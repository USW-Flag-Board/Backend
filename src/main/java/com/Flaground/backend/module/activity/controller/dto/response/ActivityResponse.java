package com.Flaground.backend.module.activity.controller.dto.response;

import com.Flaground.backend.module.activity.domain.enums.ActivityStatus;
import com.Flaground.backend.module.activity.domain.enums.ActivityType;
import com.Flaground.backend.module.activity.domain.enums.Semester;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityResponse {
    private Long id;
    private String name;
    private String leader;
    private ActivityType type;
    private ActivityStatus status;
    private String semester;

    @QueryProjection
    public ActivityResponse(Long id, String name, String leader, ActivityType type,
                            ActivityStatus status, Semester semester) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.type = type;
        this.status = status;
        this.semester = semester.toString();
    }
}
