package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Project extends Activity {
    @Column
    private String githubLink;

    @Builder
    public Project(Long id, String name, String description, Member leader, ActivityType activityType,
                   Proceed proceed, Status status, String githubLink) {
        super(id, name, description, leader, activityType, proceed, status);
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
                .status(Status.RECRUIT)
                .build();
    }
}