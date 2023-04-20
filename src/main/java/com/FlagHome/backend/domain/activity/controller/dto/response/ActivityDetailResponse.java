package com.FlagHome.backend.domain.activity.controller.dto.response;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.Semester;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityDetailResponse {
    private Long id;
    private String name;
    private String description;
    private String leader;
    private ActivityType activityType;
    private ActivityStatus status;
    private Semester semester;
    private LocalDateTime createdAt;

    @Builder
    public ActivityDetailResponse(Long id, String name, String description, String leader, ActivityType activityType,
                                  ActivityStatus status, Semester semester, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.activityType = activityType;
        this.status = status;
        this.semester = semester;
        this.createdAt = createdAt;
    }
}
