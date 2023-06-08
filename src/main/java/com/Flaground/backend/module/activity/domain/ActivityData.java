package com.Flaground.backend.module.activity.domain;

import com.Flaground.backend.module.activity.domain.enums.ActivityType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityData {
    private String name;
    private String description;
    private ActivityType type;
    private ActivityInfo info;

    @Builder
    public ActivityData(String name, String description, ActivityType type, ActivityInfo info) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.info = info;
    }
}
