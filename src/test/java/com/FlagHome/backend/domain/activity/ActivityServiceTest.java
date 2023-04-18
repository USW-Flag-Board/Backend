package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityapply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
import com.FlagHome.backend.domain.activity.controller.dto.response.ActivityResponse;
import com.FlagHome.backend.domain.activity.controller.dto.response.GetAllActivitiesResponse;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityStatus;
import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
import com.FlagHome.backend.domain.activity.entity.enums.BookUsage;
import com.FlagHome.backend.domain.activity.mapper.ActivityMapper;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipantResponse;
import com.FlagHome.backend.domain.activity.memberactivity.entity.MemberActivity;
import com.FlagHome.backend.domain.activity.memberactivity.repository.MemberActivityRepository;
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

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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

    @Nested
    @DisplayName("활동 상세보기 테스트")
    class getActivityTest {
        @Test
        @DisplayName("활동 상세보기 성공 테스트")
        void getActivitySuccessTest() {
            // given
            final ActivityType project = ActivityType.PROJECT;
            final ActivityStatus activityStatus = ActivityStatus.RECRUIT;

            Member member = memberRepository.save(Member.builder().build());
            Activity activity = activityRepository.saveAndFlush(Project.builder()
                    .leader(member)
                    .activityType(project)
                    .status(activityStatus)
                    .semester(LocalDateTime.now().getMonthValue())
                    .build());

            // when
            ActivityResponse activityResponse = activityService.getActivity(activity.getId());

            // then
            assertThat(activityResponse.getId()).isEqualTo(activity.getId());
            assertThat(activityResponse.getActivityType()).isEqualTo(project);
            assertThat(activityResponse.getActivityStatus()).isEqualTo(activityStatus);
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
                .semester(LocalDateTime.now().getMonthValue())
                .build();

        Activity study = Study.builder()
                .leader(member)
                .activityType(studyType)
                .semester(LocalDateTime.now().getMonthValue())
                .build();

        Activity mentoring = Mentoring.builder()
                .leader(member)
                .activityType(mentoringType)
                .semester(LocalDateTime.now().getMonthValue())
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
                            .semester(LocalDateTime.now().getMonthValue())
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
            List<ActivityApplyResponse> activityApplyResponses = activityService.getAllActivityApplies(member.getId(), activity.getId());

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
                                    .semester(LocalDateTime.now().getMonthValue())
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
            activity = activityRepository.save(Study.builder().semester(LocalDateTime.now().getMonthValue()).leader(leader).build());
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

            Activity activity = activityMapper.toActivity(activityRequest);

            // when
            Project project = (Project) activityService.create(member.getId(), activity);

            // then
            assertThat(project.getId()).isNotNull();
            assertThat(project.getSemester()).isNotNull();
            assertThat(project.getActivityType()).isEqualTo(activityType);
            assertThat(project.getGithubLink()).isEqualTo(githubLink);
        }

        @Test
        @DisplayName("멘토링 만들기 테스트")
        void createMentoringTest() {
            // given
            ActivityType activityType = ActivityType.MENTORING;
            BookUsage bookUsage = BookUsage.USE;
            String bookName = "토비의 스프링";

            ActivityRequest activityRequest = ActivityRequest.builder()
                    .activityType(activityType)
                    .bookUsage(bookUsage)
                    .bookName(bookName)
                    .build();

            Activity activity = activityMapper.toActivity(activityRequest);

            // when
            Mentoring mentoring = (Mentoring) activityService.create(member.getId(), activity);

            // then
            assertThat(mentoring.getId()).isNotNull();
            assertThat(mentoring.getSemester()).isNotNull();
            assertThat(mentoring.getActivityType()).isEqualTo(activityType);
            assertThat(mentoring.getBookUsage()).isEqualTo(bookUsage);
            assertThat(mentoring.getBookName()).isEqualTo(bookName);
        }

        @Test
        @DisplayName("스터디 만들기 테스트")
        void createStudyTest() {
            // given
            ActivityType activityType = ActivityType.STUDY;
            BookUsage bookUsage = BookUsage.NOT_USE;
            String bookName = "";

            ActivityRequest activityRequest = ActivityRequest.builder()
                    .activityType(activityType)
                    .bookUsage(bookUsage)
                    .bookName(bookName)
                    .build();

            Activity activity = activityMapper.toActivity(activityRequest);

            // when
            Study study = (Study) activityService.create(member.getId(), activity);

            // then
            assertThat(study.getId()).isNotNull();
            assertThat(study.getSemester()).isNotNull();
            assertThat(study.getActivityType()).isEqualTo(activityType);
            assertThat(study.getBookUsage()).isEqualTo(bookUsage);
            assertThat(study.getBookName()).isEqualTo(bookName);
        }
    }

    @Nested
    @DisplayName("활동 수정하기 테스트")
    class updateActivityTest {
        private Member member;

        @BeforeEach
        void testSetUp() {
            member = memberRepository.save(Member.builder().build());
        }

        @Test
        @DisplayName("프로젝트 수정하기 테스트")
        void updateProjectTest() {
            // given
            String name = "name";
            String changeName = "changed name";

            String link = "link";
            String changeLink = "changed link";

            Project project = activityRepository.save(Project.builder()
                            .leader(member)
                            .name(name)
                            .githubLink(link)
                            .semester(LocalDateTime.now().getMonthValue())
                            .build());

            ActivityRequest activityRequest = ActivityRequest.builder()
                    .name(changeName)
                    .githubLink(changeLink)
                    .build();

            // when
            activityService.updateProject(member.getId(), project.getId(), activityRequest);
            entityManager.clear();

            // then
            Project updatedProject = (Project) activityRepository.findById(project.getId()).get();
            assertThat(project.getName()).isNotEqualTo(updatedProject.getName());
            assertThat(project.getGithubLink()).isNotEqualTo(updatedProject.getGithubLink());
        }

        @Test
        @DisplayName("스터디 수정하기 테스트")
        void updateStudyTest() {
            // given
            String name = "name";
            String changeName = "changed name";

            BookUsage bookUsage = BookUsage.NOT_USE;
            BookUsage changeBookUsage = BookUsage.USE;

            String bookName = "book";
            String changeBookName = "changed book";

            Study study = activityRepository.save(Study.builder()
                            .leader(member)
                            .name(name)
                            .bookUsage(bookUsage)
                            .bookName(bookName)
                            .semester(LocalDateTime.now().getMonthValue())
                            .build());

            ActivityRequest activityRequest = ActivityRequest.builder()
                    .name(changeName)
                    .bookUsage(changeBookUsage)
                    .bookName(changeBookName)
                    .build();

            // when
            activityService.updateStudy(member.getId(), study.getId(), activityRequest);
            entityManager.clear();

            // then
            Study updatedStudy = (Study) activityRepository.findById(study.getId()).get();
            assertThat(study.getName()).isNotEqualTo(updatedStudy.getName());
            assertThat(study.getBookUsage()).isNotEqualTo(updatedStudy.getBookUsage());
            assertThat(study.getBookName()).isNotEqualTo(updatedStudy.getBookName());
        }

        @Test
        @DisplayName("멘토링 수정하기 테스트")
        void updateMentoringTest() {
            // given
            String name = "name";
            String changeName = "changed name";

            BookUsage bookUsage = BookUsage.NOT_USE;
            BookUsage changeBookUsage = BookUsage.USE;

            String bookName = "book";
            String changeBookName = "changed book";

            Mentoring mentoring = activityRepository.save(Mentoring.builder()
                    .leader(member)
                    .name(name)
                    .bookUsage(bookUsage)
                    .bookName(bookName)
                    .semester(LocalDateTime.now().getMonthValue())
                    .build());

            ActivityRequest activityRequest = ActivityRequest.builder()
                    .name(changeName)
                    .bookUsage(changeBookUsage)
                    .bookName(changeBookName)
                    .build();


            // when
            activityService.updateMentoring(member.getId(), mentoring.getId(), activityRequest);
            entityManager.clear();

            // then
            Mentoring updateMentoring = (Mentoring) activityRepository.findById(mentoring.getId()).get();
            assertThat(mentoring.getName()).isNotEqualTo(updateMentoring.getName());
            assertThat(mentoring.getBookUsage()).isNotEqualTo(updateMentoring.getBookUsage());
            assertThat(mentoring.getBookName()).isNotEqualTo(updateMentoring.getBookName());
        }
    }

    @Nested
    @DisplayName("활동장 변경하기 테스트")
    class changeLeaderTest {
        private Member member;
        private Member notLeader;
        private Activity activity;

        @BeforeEach
        void testSetUp() {
            member = memberRepository.save(Member.builder().build());
            notLeader = memberRepository.save(Member.builder().build());

            activity = activityRepository.save(Project.builder()
                    .leader(member)
                    .semester(LocalDateTime.now().getMonthValue())
                    .build());
        }

        @Test
        @DisplayName("활동장 변경하기 성공")
        void changeSuccessTest() {
            // given
            String loginId = "gmlwh124";
            Member participant = memberRepository.save(Member.builder().loginId(loginId).build());

            memberActivityRepository.saveAndFlush(MemberActivity.builder()
                    .member(participant)
                    .activity(activity)
                    .build());

            // when
            activityService.changeLeader(member.getId(), activity.getId(), loginId);

            // then
            Activity foundActivity = activityRepository.findById(activity.getId()).get();
            assertThat(foundActivity).isNotNull();
            assertThat(foundActivity.getLeader().getLoginId()).isNotEqualTo(member.getLoginId());
            assertThat(foundActivity.getLeader().getLoginId()).isEqualTo(participant.getLoginId());
        }

        @Test
        @DisplayName("활동장 변경하기 실패 - 활동장이 아님")
        void changeFailByNotLeaderTest() {
            String loginId = "gmlwh124";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.changeLeader(notLeader.getId(), activity.getId(), loginId))
                    .withMessage(ErrorCode.NOT_ACTIVITY_LEADER.getMessage());
        }

        @Test
        @DisplayName("활동장 변경하기 실패 - 활동원이 아님")
        void changeFailByNotMemberTest() {
            String wrongLoginId = "hejow124";

            assertThatThrownBy(() -> activityService.changeLeader(member.getId(), activity.getId(), wrongLoginId))
                    .isInstanceOf(CustomException.class)
                    .withFailMessage(ErrorCode.NOT_ACTIVITY_MEMBER.getMessage());
        }
    }

    @Nested
    @DisplayName("활동 마감하기 테스트")
    class closeRecruitmentTest {
        private Member member;
        private Activity activity;

        @BeforeEach
        void testSetUp() {
            member = memberRepository.save(Member.builder().build());
            activity = activityRepository.save(Study.builder()
                            .leader(member)
                            .semester(LocalDateTime.now().getMonthValue())
                            .status(ActivityStatus.RECRUIT)
                            .build());
        }

        @Test
        @DisplayName("활동 마감하기 성공")
        void closeSuccessTest() {
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

            activityApplyRepository.saveAll(Arrays.asList(apply1, apply2));

            // when
            activityService.closeRecruitment(member.getId(), activity.getId(), Arrays.asList(loginId1, loginId2));

            // then
            List<ActivityApplyResponse> allActivityApplies = activityService.getAllActivityApplies(member.getId(), activity.getId());
            List<ParticipantResponse> participantResponses = memberActivityRepository.getAllParticipantByActivityId(activity.getId());
            Activity findActivity = activityRepository.findById(activity.getId()).get();
            assertThat(allActivityApplies.isEmpty()).isTrue();
            assertThat(participantResponses.size()).isEqualTo(2);
            assertThat(findActivity).isNotNull();
            assertThat(findActivity.getStatus()).isEqualTo(ActivityStatus.ON);
        }

        @Test
        @DisplayName("활동 마감실패 - 활동장이 아님")
        void closeFailByNotLeaderTest() {
            Member notLeader = memberRepository.save(Member.builder().build());
            List<String> list = new ArrayList<>();

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.closeRecruitment(notLeader.getId(), activity.getId(), list))
                    .withMessage(ErrorCode.NOT_ACTIVITY_LEADER.getMessage());
        }
    }

    @Nested
    @DisplayName("활동 다시 열기 테스트")
    class reopenRecruitmentTest {
        private Member member;
        private Activity activity;

        @BeforeEach
        void testSetUp() {
            member = memberRepository.save(Member.builder().build());
            activity = activityRepository.save(Study.builder()
                    .leader(member)
                    .semester(LocalDateTime.now().getMonthValue())
                    .status(ActivityStatus.RECRUIT)
                    .build());
        }

        @Test
        @DisplayName("활동 다시 열기 성공")
        void reopenSuccessTest() {
            // given
            MemberActivity memberActivity1 = MemberActivity.builder().activity(activity).build();
            MemberActivity memberActivity2 = MemberActivity.builder().activity(activity).build();

            memberActivityRepository.saveAll(Arrays.asList(memberActivity1, memberActivity2));

            // when
            activityService.reopenRecruitment(member.getId(), activity.getId());

            // then
            List<ParticipantResponse> responses = memberActivityRepository.getAllParticipantByActivityId(activity.getId());
            Activity findActivity = activityRepository.findById(activity.getId()).get();
            assertThat(responses.isEmpty()).isTrue();
            assertThat(findActivity).isNotNull();
            assertThat(findActivity.getStatus()).isEqualTo(ActivityStatus.RECRUIT);
        }

        @Test
        @DisplayName("활동 다시 열기 실패 - 활동장이 아님")
        void reopenFailByNotLeader() {
            Member notLeader = memberRepository.save(Member.builder().build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.reopenRecruitment(notLeader.getId(), activity.getId()))
                    .withMessage(ErrorCode.NOT_ACTIVITY_LEADER.getMessage());
        }
    }

    @Nested
    @DisplayName("활동 마감하기 테스트")
    class finishActivityTest {
        private Member member;
        private Activity activity;

        @BeforeEach
        void testSetUp() {
            member = memberRepository.save(Member.builder().build());
            activity = activityRepository.save(Study.builder()
                    .leader(member)
                    .semester(LocalDateTime.now().getMonthValue())
                    .status(ActivityStatus.RECRUIT)
                    .build());
        }

        @Test
        @DisplayName("활동 마감하기 성공")
        void finishSuccessTest() {
            // given

            // when
            activityService.finishActivity(member.getId(), activity.getId());

            // then
            Activity findActivity = activityRepository.findById(activity.getId()).get();
            assertThat(findActivity).isNotNull();
            assertThat(findActivity.getStatus()).isEqualTo(ActivityStatus.OFF);
        }

        @Test
        @DisplayName("활동 다시 열기 실패 - 활동장이 아님")
        void finishFailByNotLeader() {
            Member notLeader = memberRepository.save(Member.builder().build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> activityService.finishActivity(notLeader.getId(), activity.getId()))
                    .withMessage(ErrorCode.NOT_ACTIVITY_LEADER.getMessage());
        }
    }

    @Nested
    @DisplayName("활동 삭제하기 테스트")
    class deleteActivityTest {
        private Member member;
        private Activity activity;

        @BeforeEach
        void testSetUp() {
            member = memberRepository.save(Member.builder().build());
            activity = activityRepository.save(Study.builder()
                    .leader(member)
                    .semester(LocalDateTime.now().getMonthValue())
                    .status(ActivityStatus.RECRUIT)
                    .build());
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
        Activity activity = activityRepository.save(Project.builder().semester(LocalDateTime.now().getMonthValue()).build());
        Member member = memberRepository.save(Member.builder().build());

        activityService.applyActivity(member.getId(), activity.getId());

        // when
        activityService.cancelApply(member.getId(), activity.getId());

        // then
        boolean check = activityService.checkApply(member.getId(), activity.getId());
        assertThat(check).isFalse();
    }
}