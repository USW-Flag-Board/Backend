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
    private List<ActivityResponse> allActivities;

    public GetAllActivitiesResponse(List<ActivityResponse> allActivities) {
        this.allActivities = allActivities;
    }

    public static GetAllActivitiesResponse from(List<ActivityResponse> allActivities) {
        return new GetAllActivitiesResponse(allActivities);
    }
}
