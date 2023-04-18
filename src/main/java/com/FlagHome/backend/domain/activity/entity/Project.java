package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.Proceed;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends Activity {
    @Column
    private String githubLink;

    @Builder
    public Project(String name, String description, Member leader, ActivityType activityType,
                   Proceed proceed, ActivityStatus status, int semester, String githubLink) {
        super(name, description, leader, activityType, proceed, status, semester);
        this.githubLink = githubLink;
    }

    public void updateProject(ActivityRequest activityRequest) {
        super.update(activityRequest);
        this.githubLink = activityRequest.getGithubLink();
    }

    public static Project from(ActivityRequest activityRequest) {
        return Project.builder()
                .name(activityRequest.getName())
                .description(activityRequest.getDescription())
                .proceed(activityRequest.getProceed())
                .githubLink(activityRequest.getGithubLink())
                .activityType(ActivityType.PROJECT)
                .status(ActivityStatus.RECRUIT)
                .semester(LocalDateTime.now().getMonthValue())
                .build();
    }
}