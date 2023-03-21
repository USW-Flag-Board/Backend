package com.FlagHome.backend.domain.activity.mapper;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.controller.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ActivityMapper {
    public Activity toActivity(ActivityRequest activityRequest) {
        Activity activity = Arrays.stream(ActivityType.values())
                .filter(type -> type == activityRequest.getActivityType())
                .findFirst()
                .map(type -> type.toEntity(activityRequest))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_SUPPORT_ACTIVITY));
        return activity;
    }
}
