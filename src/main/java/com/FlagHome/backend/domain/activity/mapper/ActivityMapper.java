package com.FlagHome.backend.domain.activity.mapper;

import com.FlagHome.backend.domain.activity.controller.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Arrays;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ActivityMapper {
    default Activity toActivity(ActivityRequest activityRequest) {
        ActivityType matchingType = Arrays.stream(ActivityType.values())
                .filter(type -> type == activityRequest.getActivityType())
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_SUPPORT_ACTIVITY));
        return matchingType.toEntity(activityRequest);
    }
}
