package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ActivityServiceTest {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private MemberRepository memberRepository;

//    @Test
//    @DisplayName("프로젝트 생성 테스트")
//    void createProjectTest() {
//        // given
//        Member member = memberRepository.save(Member.builder().build());
//
//        String name = "테스트 활동";
//        String description = "테스트 설명";
//        String githubLink = "github.com";
//
//        ActivityRequest createProjectRequest = ActivityRequest.builder()
//                .name(name)
//                .description(description)
//                .githubLink(githubLink)
//                .build();
//
//        // when
//        Activity project = activityService.create(member.getId(), createProjectRequest);
//
//        // then
//        assertThat(project.getLeader().getId()).isEqualTo(member.getId());
//        assertThat(project.getName()).isEqualTo(name);
//        assertThat(project.getDescription()).isEqualTo(description);
//        assertThat(project.getGithubLink()).isEqualTo(githubLink);
//        assertThat(project.getBookUsage()).isNull();
//        assertThat(project.getClass()).isAssignableFrom(Project.class);
//    }
}
