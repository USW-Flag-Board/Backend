package com.FlagHome.backend.domain.activity.dto;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.Status;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ActivityResponse {
    private long id;
    private String name;
    private String leader;
    private ActivityType activityType;
    private Status status;
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public ActivityResponse(long id, String name, String leader, ActivityType activityType, Status status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.leader = leader;
        this.activityType = activityType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getYear() {
        return String.valueOf(this.createdAt.getYear());
    }

    public String getSeason() { // 꼭 개선하기
        final int month = this.createdAt.getMonthValue();

        if (3 <= month && month < 6) {
            return "1학기";
        } else if (6 <= month && month < 9) {
            return "여름방학";
        } else if (9 <= month && month < 12) {
            return "2학기";
        } else {
            return "겨울방학";
        }
    }
}
