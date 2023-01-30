package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityapply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.dto.GetAllActivitiesResponse;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.domain.member.Major;
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

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class ActivityServiceTest {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityApplyRepository activityApplyRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

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
        String year = "2023";
        ActivityType projectType = ActivityType.PROJECT;
        ActivityType mentoringType = ActivityType.MENTORING;
        ActivityType studyType = ActivityType.STUDY;

        Member member = memberRepository.save(Member.builder().build());

        Activity project = Project.builder()
                .leader(member)
                .activityType(projectType)
                .season(LocalDateTime.now())
                .build();

        Activity study = Study.builder()
                .leader(member)
                .activityType(studyType)
                .season(LocalDateTime.now())
                .build();

        Activity mentoring = Mentoring.builder()
                .leader(member)
                .activityType(mentoringType)
                .season(LocalDateTime.now())
                .build();

        activityRepository.saveAll(Arrays.asList(project, study, mentoring));

        // when
        GetAllActivitiesResponse getAllActivitiesResponse = activityService.getAllActivities();

        // then
        assertThat(getAllActivitiesResponse.getAllActivities().isEmpty()).isFalse();
        assertThat(getAllActivitiesResponse.getAllActivities().containsKey(year)).isTrue();
        assertThat(getAllActivitiesResponse.getAllActivities().get(year).get(projectType)).isNotNull();
        assertThat(getAllActivitiesResponse.getAllActivities().get(year).get(mentoringType)).isNotNull();
        assertThat(getAllActivitiesResponse.getAllActivities().get(year).get(studyType)).isNotNull();
    }

    @Nested
    @DisplayName("활동 신청 가져오기 테스트")
    class getAllActivityAppliesTest {

        private Member member;
        private Activity activity;

        @BeforeEach
        void testSetUp() {
            ActivityType project = ActivityType.PROJECT;

            member = memberRepository.save(Member.builder().build());
            activity = activityRepository.save(Project.builder()
                            .leader(member)
                            .activityType(project)
                            .season(LocalDateTime.now())
                            .build());
        }

        @Test
        @DisplayName("신청 가져오기 성공")
        void getAppliesSuccessTest() {
            // given
            Member applier1 = memberRepository.save(Member.builder()
                    .loginId("gmlwh124")
                    .name("문희조")
                    .major(Major.컴퓨터SW)
                    .build());

            Member applier2 = memberRepository.save(Member.builder()
                    .loginId("hejow124")
                    .name("희조문")
                    .major(Major.경영)
                    .build());

            ActivityApply apply1 = ActivityApply.builder()
                    .member(applier1)
                    .activity(activity)
                    .build();

            ActivityApply apply2 = ActivityApply.builder()
                    .member(applier2)
                    .activity(activity)
                    .build();

            activityApplyRepository.saveAll(Arrays.asList(apply1, apply2));
            entityManager.clear();

            // when
            List<ActivityApplyResponse> activityApplyResponses = activityService.getAllActivityApplies(member.getId(), activity.getId());

            // then
            ActivityApplyResponse response = activityApplyResponses.get(0);
            assertThat(activityApplyResponses.size()).isEqualTo(2);
            assertThat(response.getName()).isNotNull();
            assertThat(response.getLoginId()).isNotNull();
            assertThat(response.getMajor()).isNotNull();
            assertThat(response.getId()).isNotNull();
        }
        
        @Test
        @DisplayName("신청 가져오기 실패")
        void getAppliesFailTest() {
            Member notLeader = memberRepository.save(Member.builder().build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.getAllActivityApplies(notLeader.getId(), activity.getId()))
                    .withMessage(ErrorCode.NOT_ACTIVITY_LEADER.getMessage());
        }
    }

    @Test
    @DisplayName("활동 신청 체크 테스트")
    void checkApplyTest() {
        // given
        Member applier = memberRepository.save(Member.builder().build());
        Member notApplier = memberRepository.save(Member.builder().build());

        Activity activity = activityRepository.save(Project.builder()
                                    .season(LocalDateTime.now())
                                    .build());

        activityApplyRepository.save(ActivityApply.builder()
                                    .member(applier)
                                    .activity(activity)
                                    .build());

        // when
        boolean shouldBeTrue = activityService.checkApply(applier.getId(), activity.getId());
        boolean shouldBeFalse = activityService.checkApply(notApplier.getId(), activity.getId());

        // then
        assertThat(shouldBeTrue).isTrue();
        assertThat(shouldBeFalse).isFalse();
    }

    @Nested
    @DisplayName("활동 신청하기 테스트")
    class applyActivityTest {
        private Member leader;
        private Member applier;
        private Activity activity;

        @BeforeEach
        void testSetUp() {
            leader = memberRepository.save(Member.builder().build());
            applier = memberRepository.save(Member.builder().build());
            activity = activityRepository.save(Study.builder().season(LocalDateTime.now()).leader(leader).build());
        }

        @Test
        @DisplayName("활동 신청 성공 테스트")
        void applySuccessTest() {
            // given

            // when
            ActivityApply apply = activityService.applyActivity(applier.getId(), activity.getId());

            // then
            assertThat(apply.getMember().getId()).isEqualTo(applier.getId());
            assertThat(apply.getActivity().getId()).isEqualTo(activity.getId());
        }

        @Test
        @DisplayName("활동 신청 실패 테스트 : 이미 신청")
        void applyFailByAlreadyAppliedTest() {
            activityApplyRepository.save(ActivityApply.builder()
                    .activity(activity)
                    .member(applier)
                    .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.applyActivity(applier.getId(), activity.getId()))
                    .withMessage(ErrorCode.ALREADY_APPLIED.getMessage());
        }

        @Test
        @DisplayName("활동 신청 실패 테스트 : 없는 활동")
        void applyFailByNotExistTest() {
            long noneActivityId = 1204L;

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.applyActivity(applier.getId(), noneActivityId))
                    .withMessage(ErrorCode.ACTIVITY_NOT_FOUND.getMessage());
        }
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
