package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.dto.GetAllActivitiesResponse;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class ActivityServiceTest {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("활동 상세보기 테스트")
    class getActivityTest {
        @Test
        @DisplayName("활동 상세보기 성공 테스트")
        void getActivitySuccessTest() {
            // given
            final ActivityType project = ActivityType.PROJECT;
            final Status status = Status.RECRUIT;

            Member member = memberRepository.save(Member.builder().build());
            Activity activity = activityRepository.saveAndFlush(Project.builder()
                    .leader(member)
                    .activityType(project)
                    .status(status)
                    .season(LocalDateTime.now())
                    .build());

            // when
            ActivityResponse activityResponse = activityService.getActivity(activity.getId());

            // then
            assertThat(activityResponse.getId()).isEqualTo(activity.getId());
            assertThat(activityResponse.getActivityType()).isEqualTo(project);
            assertThat(activityResponse.getStatus()).isEqualTo(status);
        }

        @Test
        @DisplayName("활동 상세보기 실패 테스트")
        void getActivityFailTest() {
            // given
            final long noneId = 1L;

            // when, then
            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.getActivity(noneId))
                    .withMessage(ErrorCode.ACTIVITY_NOT_FOUND.getMessage());
        }
    }

    @Test
    @DisplayName("모든 활동 가져오기 테스트")
    void getAllActivitiesTest() {
        // given
        Member member = memberRepository.save(Member.builder().build());

        Activity project = Project.builder()
                .leader(member)
                .activityType(ActivityType.PROJECT)
                .season(LocalDateTime.now())
                .build();

        Activity study = Study.builder()
                .leader(member)
                .activityType(ActivityType.STUDY)
                .season(LocalDateTime.now())
                .build();

        Activity mentoring = Mentoring.builder()
                .leader(member)
                .activityType(ActivityType.STUDY)
                .season(LocalDateTime.now())
                .build();

        activityRepository.saveAll(Arrays.asList(project, study, mentoring));

        // when
        GetAllActivitiesResponse getAllActivitiesResponse = activityService.getAllActivities();

        // then

    }

    @Nested
    @DisplayName("활동 만들기 테스트")
    class createActivityTest {
        private Member member;

        @BeforeEach
        void testSetUp() {
            String loginId = "gmlwh124";
            String email = "gmlwh124@suwon.ac.kr";

            member = memberRepository.save(Member.builder()
                            .loginId(loginId)
                            .email(email)
                            .build());
        }

        @Test
        @DisplayName("프로젝트 만들기 테스트")
        void createProjectTest() {
            // given
            ActivityType activityType = ActivityType.PROJECT;
            String githubLink = "github.com/hejow";

            ActivityRequest activityRequest = ActivityRequest.builder()
                    .activityType(activityType)
                    .githubLink(githubLink)
                    .build();

            // when
            Project project = (Project) activityService.create(member.getId(), activityRequest);

            // then
            assertThat(project.getId()).isNotNull();
            assertThat(project.getSeason()).isNotNull();
            assertThat(project.getActivityType()).isEqualTo(activityType);
            assertThat(project.getGithubLink()).isEqualTo(githubLink);
        }

        @Test
        @DisplayName("멘토링 만들기 테스트")
        void createMentoringTest() {
            // given
            ActivityType activityType = ActivityType.MENTORING;
            BookUsage bookUsage = BookUsage.사용;
            String bookName = "토비의 스프링";

            ActivityRequest activityRequest = ActivityRequest.builder()
                    .activityType(activityType)
                    .bookUsage(bookUsage)
                    .bookName(bookName)
                    .build();

            // when
            Mentoring mentoring = (Mentoring) activityService.create(member.getId(), activityRequest);

            // then
            assertThat(mentoring.getId()).isNotNull();
            assertThat(mentoring.getSeason()).isNotNull();
            assertThat(mentoring.getActivityType()).isEqualTo(activityType);
            assertThat(mentoring.getBookUsage()).isEqualTo(bookUsage);
            assertThat(mentoring.getBookName()).isEqualTo(bookName);
        }

        @Test
        @DisplayName("스터디 만들기 테스트")
        void createStudyTest() {
            // given
            ActivityType activityType = ActivityType.STUDY;
            BookUsage bookUsage = BookUsage.미사용;
            String bookName = "";

            ActivityRequest activityRequest = ActivityRequest.builder()
                    .activityType(activityType)
                    .bookUsage(bookUsage)
                    .bookName(bookName)
                    .build();

            // when
            Study study = (Study) activityService.create(member.getId(), activityRequest);

            // then
            assertThat(study.getId()).isNotNull();
            assertThat(study.getSeason()).isNotNull();
            assertThat(study.getActivityType()).isEqualTo(activityType);
            assertThat(study.getBookUsage()).isEqualTo(bookUsage);
            assertThat(study.getBookName()).isEqualTo(bookName);
        }
    }
}
