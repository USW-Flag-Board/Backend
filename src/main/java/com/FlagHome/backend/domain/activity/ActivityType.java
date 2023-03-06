package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
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