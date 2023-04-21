package com.FlagHome.backend.domain.activity.controller.dto.response;

import com.FlagHome.backend.domain.activity.entity.enums.*;
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
    private String githubLink;
    private BookUsage bookUsage;
    private String bookName;
    private ActivityType activityType;
    private ActivityStatus status;
    private String semester;
    private LocalDateTime createdAt;

    @Builder
    public ActivityDetailResponse(Long id, String name, String description, String leader, Proceed proceed, String githubLink, BookUsage bookUsage,
                                  String bookName, ActivityType activityType, ActivityStatus status, Semester semester, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.proceed = proceed;
        this.githubLink = githubLink;
        this.bookUsage = bookUsage;
        this.bookName = bookName;
        this.activityType = activityType;
        this.status = status;
        this.semester = semester.getSemester();
        this.createdAt = createdAt;
    }
}
