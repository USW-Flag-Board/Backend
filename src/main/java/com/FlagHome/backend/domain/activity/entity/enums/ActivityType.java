package com.FlagHome.backend.domain.activity.entity.enums;

import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
    PROJECT("프로젝트", Project::from),
    STUDY("스터디", Study::from),
    MENTORING("멘토링", Mentoring::from);

    private final String type;
    private final Function<ActivityRequest, Activity> expression;

    public Activity toEntity(ActivityRequest activityRequest) {
        return expression.apply(activityRequest);
    }
}
