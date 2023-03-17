package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.controller.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import lombok.Getter;

import java.util.function.Function;

@Getter
public enum ActivityType {
    PROJECT("프로젝트", Project::from),
    STUDY("스터디", Study::from),
    MENTORING("멘토링", Mentoring::from);

    ActivityType(String type, Function<ActivityRequest, Activity> expression) {
        this.type = type;
        this.expression = expression;
    }

    private String type;
    private Function<ActivityRequest, Activity> expression;

    public Activity toEntity(ActivityRequest activityRequest) {
        return expression.apply(activityRequest);
    }
}