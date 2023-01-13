package com.FlagHome.backend.domain.activity.entity;

import com.FlagHome.backend.domain.activity.Proceed;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.activity.dto.CreateActivityRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Project extends Activity {

    @Builder
    public Project(Long id, String name, String description, Member leader, Proceed proceed,
                   String githubLink, Boolean isBookExist, String bookName, Status status) {
        super(id, name, description, leader, proceed, githubLink, isBookExist, bookName, status);
    }

    public static Project of(Member member, CreateActivityRequest createProjectRequest) {
        return Project.builder()
                .leader(member)
                .name(createProjectRequest.getName())
                .description(createProjectRequest.getDescription())
                .proceed(createProjectRequest.getProceed())
                .githubLink(createProjectRequest.getGithubLink())
                .status(Status.RECRUIT)
                .build();
    }
}