package com.FlagHome.backend.domain.activity.mapper;

import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
import com.FlagHome.backend.domain.activity.controller.dto.response.ActivityDetailResponse;
import com.FlagHome.backend.domain.activity.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ActivityMapper {
    default Activity toActivity(ActivityRequest activityRequest) {
        return activityRequest.getActivityType().toEntity(activityRequest);
    }

    @Mapping(source = "activity.leader.name", target = "leader")
    ActivityDetailResponse toDetailResponse(Activity activity);
}
