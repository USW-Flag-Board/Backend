package com.FlagHome.backend.domain.activity.dto;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.Status;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ActivityResponse {
    private long id;
    private String name;
    private String leader;
    private ActivityType activityType;
    private Status status;

    @Builder
    @QueryProjection
    public ActivityResponse(long id, String name, String leader, ActivityType activityType, Status status) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.activityType = activityType;
        this.status = status;
    }
}
