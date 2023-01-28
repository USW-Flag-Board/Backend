package com.FlagHome.backend.domain.activity.dto;

import com.FlagHome.backend.domain.activity.ActivityType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllActivitiesResponse {
    @Schema(name = "모든 활동 정보")
    private Map<String, Map<ActivityType, List<ActivityResponse>>> allActivities;

    public static GetAllActivitiesResponse from(Map<String, Map<ActivityType, List<ActivityResponse>>> allActivities) {
        return GetAllActivitiesResponse.builder()
                .allActivities(allActivities)
                .build();
    }
}
