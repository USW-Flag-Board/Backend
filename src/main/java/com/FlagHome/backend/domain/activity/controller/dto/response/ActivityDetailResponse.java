package com.FlagHome.backend.domain.activity.controller.dto.response;

import com.FlagHome.backend.domain.activity.entity.ActivityInfo;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityDetailResponse {
    private Long id;
    private String name;
    private String description;
    private String leader;
    private Proceed proceed;
    private String githubURL;
    private BookUsage bookUsage;
    private String bookName;
    private ActivityType type;
    private ActivityStatus status;
    private String semester;
    private LocalDateTime createdAt;

    @Builder
    public ActivityDetailResponse(Long id, String name, String description, String leader, ActivityInfo info,
                                  ActivityType type, ActivityStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.proceed = info.getProceed();
        this.githubURL = info.getGithubURL();
        this.bookUsage = info.getBookUsage();
        this.bookName = info.getBookName();
        this.type = type;
        this.status = status;
        this.semester = info.getSemester().toString();
        this.createdAt = createdAt;
    }
}
