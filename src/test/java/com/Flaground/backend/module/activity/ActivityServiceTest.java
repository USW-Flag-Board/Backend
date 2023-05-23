package com.Flaground.backend.module.activity;

import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.Flaground.backend.module.activity.domain.ActivityApply;
import com.Flaground.backend.module.activity.domain.repository.ActivityApplyRepository;
import com.Flaground.backend.module.activity.controller.dto.response.GetAllActivitiesResponse;
import com.Flaground.backend.module.activity.domain.Activity;
import com.Flaground.backend.module.activity.domain.ActivityInfo;
import com.Flaground.backend.module.activity.domain.enums.ActivityStatus;
import com.Flaground.backend.module.activity.domain.enums.ActivityType;
import com.Flaground.backend.module.activity.domain.enums.Proceed;
import com.Flaground.backend.module.activity.controller.mapper.ActivityMapper;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.Flaground.backend.module.activity.domain.repository.MemberActivityRepository;
import com.Flaground.backend.module.activity.domain.repository.ActivityRepository;
import com.Flaground.backend.module.activity.service.ActivityService;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private MemberActivityRepository memberActivityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 활동_상세보기_실패_테스트() {
        // given
        final Long noneId = 1L;

        // when, then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> activityService.getActivity(noneId))
                .withMessage(ErrorCode.ACTIVITY_NOT_FOUND.getMessage());
    }

    @Test
    void 모든_활동_가져오기_테스트() {
        // given
        ActivityType projectType = ActivityType.PROJECT;
        ActivityType mentoringType = ActivityType.MENTORING;
        ActivityType studyType = ActivityType.STUDY;
        ActivityInfo info = ActivityInfo.builder().build();

        Member member = memberRepository.save(Member.builder().build());

        Activity project = Activity.builder().leader(member).type(projectType).info(info).build();
        Activity mentoring = Activity.builder().leader(member).type(mentoringType).info(info).build();
        Activity study = Activity.builder().leader(member).type(studyType).info(info).build();

        activityRepository.saveAll(Arrays.asList(project, study, mentoring));

        // when
        GetAllActivitiesResponse response = activityService.getAllActivities();

        // then
        assertThat(response.getAllActivities().isEmpty()).isFalse();
        assertThat(response.getAllActivities().size()).isEqualTo(3);
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
            activity = activityRepository.save(Activity.builder()
                            .leader(member)
                            .type(project)
                            .build());
        }

        @Test
        @DisplayName("신청 가져오기 성공")
        void getAppliesSuccessTest() {
            // given
            Member applier1 = memberRepository.save(Member.builder()
                    .loginId("gmlwh124")
                    .name("문희조")
                    .build());

            Member applier2 = memberRepository.save(Member.builder()
                    .loginId("hejow124")
                    .name("희조문")
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
            List<ActivityApplyResponse> activityApplyResponses = activityService.getAllApplies(member.getId(), activity.getId());

            // then
            ActivityApplyResponse response = activityApplyResponses.get(0);
            assertThat(activityApplyResponses.size()).isEqualTo(2);
            assertThat(response.getName()).isNotNull();
            assertThat(response.getLoginId()).isNotNull();
            assertThat(response.getId()).isNotNull();
        }
        
        @Test
        @DisplayName("신청 가져오기 실패")
        void getAppliesFailTest() {
            Member notLeader = memberRepository.save(Member.builder().build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.getAllApplies(notLeader.getId(), activity.getId()))
                    .withMessage(ErrorCode.NOT_ACTIVITY_LEADER.getMessage());
        }
    }

    @Test
    @DisplayName("활동 신청 체크 테스트")
    void checkApplyTest() {
        // given
        Member applier = memberRepository.save(Member.builder().build());
        Member notApplier = memberRepository.save(Member.builder().build());

        Activity activity = activityRepository.save(Activity.builder().build());

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
            activity = activityRepository.save(Activity.builder().leader(leader).build());
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


    @Test
    void 활동_만들기_테스트() {

    }

    @Test
    void 활동_수정하기_테스트() {

    }

    @Nested
    class 활동_모집_마감하기_테스트 {
        private Member member;
        private Activity activity;

        @BeforeEach
        void testSetup() {
            final String githubURL = "URL";
            final ActivityType type = ActivityType.PROJECT;
            final ActivityInfo info = ActivityInfo.builder()
                    .proceed(Proceed.ONLINE)
                    .githubURL(githubURL)
                    .build();

            member = memberRepository.save(Member.builder().build());
            activity = activityRepository.save(Activity.builder()
                    .leader(member)
                    .type(type)
                    .info(info)
                    .build());
        }

        @Test
        void 활동_모집_마감_성공_테스트() {
            // given
            String loginId1 = "gmlwh124";
            String loginId2 = "hejow124";

            Member applier1 = memberRepository.save(Member.builder().loginId(loginId1).build());
            Member applier2 = memberRepository.save(Member.builder().loginId(loginId2).build());

            ActivityApply apply1 = ActivityApply.builder()
                    .member(applier1)
                    .activity(activity)
                    .build();

            ActivityApply apply2 = ActivityApply.builder()
                    .member(applier2)
                    .activity(activity)
                    .build();

            activityApplyRepository.saveAll(List.of(apply1, apply2));

            // when
            activityService.closeRecruitment(member.getId(), activity.getId(), Arrays.asList(loginId1, loginId2));

            // then
            List<ActivityApplyResponse> allActivityApplies = activityService.getAllApplies(member.getId(), activity.getId());
            List<ParticipantResponse> participantResponses = memberActivityRepository.getAllParticipantByActivityId(activity.getId());
            Activity findActivity = activityRepository.findById(activity.getId()).get();
            assertThat(allActivityApplies.size()).isEqualTo(0);
            assertThat(participantResponses.size()).isEqualTo(2);
            assertThat(findActivity.getStatus()).isEqualTo(ActivityStatus.ON);
        }

        @Test
        void 활동_모집_마감_실패_테스트() {
            Member notLeader = memberRepository.save(Member.builder().build());
            List<String> list = new ArrayList<>();

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.closeRecruitment(notLeader.getId(), activity.getId(), list))
                    .withMessage(ErrorCode.NOT_ACTIVITY_LEADER.getMessage());
        }
    }

    @Test
    void 활동_마감하기_성공_테스트() {
        // given
        Member member = memberRepository.save(Member.builder().build());
        Activity activity = activityRepository.save(Activity.builder().leader(member).build());

        activity.closeRecruitment();

        // when
        activityService.finishActivity(member.getId(), activity.getId());

        // then
        Activity findActivity = activityRepository.findById(activity.getId()).get();
        assertThat(findActivity.getStatus()).isEqualTo(ActivityStatus.OFF);
    }

    @Nested
    @DisplayName("활동 삭제하기 테스트")
    class deleteActivityTest {
        private Member member;
        private Activity activity;

        @BeforeEach
        void testSetUp() {
            member = memberRepository.save(Member.builder().build());
            activity = activityRepository.save(Activity.builder().leader(member).build());
        }

        @Test
        @DisplayName("활동 삭제하기 성공")
        void deleteSuccessTest() {
            // given
            Member applier = memberRepository.save(Member.builder().build());

            activityService.applyActivity(applier.getId(), activity.getId());

            // when
            activityService.delete(member.getId(), activity.getId());

            // then
            Optional<Activity> findActivity = activityRepository.findById(activity.getId());
            assertThat(findActivity.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("활동 다시 열기 실패 - 활동장이 아님")
        void deleteFailByNotLeader() {
            Member notLeader = memberRepository.save(Member.builder().build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.delete(notLeader.getId(), activity.getId()))
                    .withMessage(ErrorCode.NOT_ACTIVITY_LEADER.getMessage());
        }
    }

    @Test
    @DisplayName("활동 신청 취소 테스트")
    void cancelApplyTest() {
        // given
        Activity activity = activityRepository.save(Activity.builder().build());
        Member member = memberRepository.save(Member.builder().build());

        activityService.applyActivity(member.getId(), activity.getId());

        // when
        activityService.cancelApply(member.getId(), activity.getId());

        // then
        boolean check = activityService.checkApply(member.getId(), activity.getId());
        assertThat(check).isFalse();
    }
}