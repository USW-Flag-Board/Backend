package com.FlagHome.backend.domain.activity.controller.dto.response;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetAllActivitiesResponse {
    @Schema(name = "모든 활동 정보")
    private Map<String, Map<ActivityType, List<ActivityResponse>>> allActivities;

    @Builder
    public GetAllActivitiesResponse(Map<String, Map<ActivityType, List<ActivityResponse>>> allActivities) {
        this.allActivities = allActivities;
    }

    public static GetAllActivitiesResponse from(Map<String, Map<ActivityType, List<ActivityResponse>>> allActivities) {
        return GetAllActivitiesResponse.builder()
                .allActivities(allActivities)
                .build();
    }
}
