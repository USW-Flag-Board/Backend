package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.activity.dto.CreateActivityRequest;
import com.FlagHome.backend.domain.activity.dto.UpdateActivityRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
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

    public void updateProject(UpdateActivityRequest updateActivityRequest) {
        super.update(updateActivityRequest);
        this.githubLink = updateActivityRequest.getGithubLink();
    }

    public static Project from(CreateActivityRequest createActivityRequest) {
        return Project.builder()
                .name(createActivityRequest.getName())
                .description(createActivityRequest.getDescription())
                .proceed(createActivityRequest.getProceed())
                .githubLink(createActivityRequest.getGithubLink())
                .activityType(ActivityType.PROJECT)
                .status(Status.RECRUIT)
                .build();
    }
}