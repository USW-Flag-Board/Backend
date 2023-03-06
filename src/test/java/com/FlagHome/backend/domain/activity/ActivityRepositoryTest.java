package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityapply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipantResponse;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.activity.memberactivity.entity.MemberActivity;
import com.FlagHome.backend.domain.activity.memberactivity.repository.MemberActivityRepository;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.Major;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QueryDslConfig.class)
public class ActivityRepositoryTest {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityApplyRepository activityApplyRepository;

    @Autowired
    private MemberActivityRepository memberActivityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("활동 테스트")
    class activityTest {
        @Test
        @DisplayName("활동 가져오기 테스트")
        void getActivityTest() {
            // given
            String memberName = "Hejow";
            String activityName = "이름";
            Member member = memberRepository.saveAndFlush(Member.builder()
                    .name(memberName)
                    .build());
            ActivityType activityType = ActivityType.PROJECT;

            Project project = Project.builder()
                    .name(activityName)
                    .leader(member)
                    .activityType(activityType)
                    .season(LocalDateTime.now())
                    .build();

            Activity activity = activityRepository.saveAndFlush(project);

            // when
            ActivityResponse activityResponse = activityRepository.getActivity(activity.getId()).get();

            // then
            assertThat(activity.getId()).isEqualTo(activityResponse.getId());
            assertThat(activity.getName()).isEqualTo(activityResponse.getName());
            assertThat(activity.getLeader().getName()).isEqualTo(activityResponse.getLeader());
            assertThat(activityResponse.getActivityType()).isEqualTo(activityType);
        }

        @Test
        @DisplayName("모든 활동 가져오기 테스트")
        void getAllActivitiesTest() {
            // given
            Member member = memberRepository.save(Member.builder().build());

            Project project = Project.builder()
                    .leader(member)
                    .activityType(ActivityType.PROJECT)
                    .season(LocalDateTime.now())
                    .build();

            Study study = Study.builder()
                    .leader(member)
                    .activityType(ActivityType.STUDY)
                    .season(LocalDateTime.now())
                    .build();

            Mentoring mentoring = Mentoring.builder()
                    .leader(member)
                    .activityType(ActivityType.MENTORING)
                    .season(LocalDateTime.now())
                    .build();

            activityRepository.saveAll(Arrays.asList(project, study, mentoring));

            // when
            List<ActivityResponse> activityResponseList = activityRepository.getAllActivities();

            // then
            assertThat(activityResponseList.size()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("활동 신청 테스트")
    public class activityApplyTest {
        @Test
        @DisplayName("모든 활동 신청 가져오기 테스트")
        void getAllAppliesTest() {
            // given
            Major major = Major.컴퓨터SW;

            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().major(major).build());
            Member member3 = memberRepository.save(Member.builder().major(major).build());

            Activity activity = activityRepository.saveAndFlush(Project.builder().leader(member1).season(LocalDateTime.now()).build());

            ActivityApply activityApply1 = ActivityApply.builder().member(member2).activity(activity).build();
            ActivityApply activityApply2 = ActivityApply.builder().member(member3).activity(activity).build();

            activityApplyRepository.saveAll(Arrays.asList(activityApply1, activityApply2));

            // when
            List<ActivityApplyResponse> responses = activityApplyRepository.getAllApplies(activity.getId());

            // then
            assertThat(responses.size()).isEqualTo(2);
            assertThat(responses.get(0).getId()).isNotEqualTo(responses.get(1).getId());
            assertThat(responses.get(0).getMajor()).isEqualTo(major);
        }

        @Test
        @DisplayName("모든 신청 삭제하기 테스트")
        void deleteAllAppliesTest() {
            // given
            Major major = Major.컴퓨터SW;

            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().major(major).build());
            Member member3 = memberRepository.save(Member.builder().major(major).build());

            Activity activity = activityRepository.saveAndFlush(Project.builder().leader(member1).season(LocalDateTime.now()).build());

            ActivityApply activityApply1 = ActivityApply.builder().member(member2).activity(activity).build();
            ActivityApply activityApply2 = ActivityApply.builder().member(member3).activity(activity).build();

            activityApplyRepository.saveAll(Arrays.asList(activityApply1, activityApply2));

            // when
            activityApplyRepository.deleteAllApplies(activity.getId());
            List<ActivityApplyResponse> responses = activityApplyRepository.getAllApplies(activity.getId());

            // then
            assertThat(responses.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("신청 체크 테스트")
        void checkApplyTest() {
            // given
            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().build());

            Activity activity = activityRepository.save(Study.builder().leader(member1).season(LocalDateTime.now()).build());

            activityApplyRepository.save(ActivityApply.builder()
                    .member(member2)
                    .activity(activity)
                    .build());

            // when
            boolean check1 = activityApplyRepository.checkApply(member1.getId(), activity.getId());
            boolean check2 = activityApplyRepository.checkApply(member2.getId(), activity.getId());

            // then
            assertThat(check1).isFalse();
            assertThat(check2).isTrue();
        }

        @Test
        @DisplayName("활동 신청 정보 지우기 테스트")
        void findApplyByMemberAndActivityTest() {
            // given
            Member member = memberRepository.save(Member.builder().build());
            Activity activity = activityRepository.save(Mentoring.builder().leader(member).season(LocalDateTime.now()).build());

            ActivityApply apply = activityApplyRepository.save(ActivityApply.builder()
                    .member(member)
                    .activity(activity)
                    .build());

            // when
            entityManager.clear();
            activityApplyRepository.deleteByMemberIdAndActivityId(member.getId(), activity.getId());

            // then
            Optional<ActivityApply> findApply = activityApplyRepository.findById(apply.getId());
            assertThat(findApply.isPresent()).isFalse();
        }
    }

    @Nested
    @DisplayName("멤버활동 테스트")
    class memberActivityTest {
        @Test
        @DisplayName("활동으로 지우기 테스트")
        void deleteAllByActivityTest() {
            // given
            Activity activity = activityRepository.save(Project.builder().season(LocalDateTime.now()).build());

            MemberActivity memberActivity1 = MemberActivity.builder().activity(activity).build();
            MemberActivity memberActivity2 = MemberActivity.builder().activity(activity).build();

            memberActivityRepository.saveAll(Arrays.asList(memberActivity1, memberActivity2));

            // when
            memberActivityRepository.deleteAllByActivityId(activity.getId());
            entityManager.clear();

            // then
            assertThatExceptionOfType(NoSuchElementException.class)
                    .isThrownBy(() -> memberActivityRepository.findById(activity.getId()).get());
        }

        @Test
        @DisplayName("멤버 참가활동 가져오기 테스트")
        void getAllParticipateActivityTest() {
            // given
            String loginId = "gmlwh124";
            Member member = memberRepository.save(Member.builder().loginId(loginId).build());

            Activity project = Project.builder()
                    .name("project")
                    .season(LocalDateTime.now())
                    .status(Status.ON)
                    .build();

            Activity mentoring = Mentoring.builder()
                    .name("mentoring")
                    .season(LocalDateTime.now())
                    .status(Status.RECRUIT)
                    .build();

            Activity study = Study.builder()
                    .name("study")
                    .season(LocalDateTime.now())
                    .status(Status.OFF)
                    .build();

            activityRepository.saveAll(Arrays.asList(project, mentoring, study));


            MemberActivity memberActivity1 = MemberActivity.builder()
                    .member(member)
                    .activity(project)
                    .build();

            MemberActivity memberActivity2 = MemberActivity.builder()
                    .member(member)
                    .activity(mentoring)
                    .build();

            MemberActivity memberActivity3 = MemberActivity.builder()
                    .member(member)
                    .activity(study)
                    .build();

            memberActivityRepository.saveAll(Arrays.asList(memberActivity1, memberActivity2, memberActivity3));

            // when
            List<ParticipateResponse> responseList = memberActivityRepository.getAllActivitiesOfMember(loginId);

            // then
            assertThat(responseList.size()).isEqualTo(3);
            assertThat(responseList.get(0).getId()).isNotNull();
            assertThat(responseList.get(0).getYear()).isInstanceOf(Integer.class);
            assertThat(responseList.get(0).getSeason()).isNotNull();
            assertThat(responseList.get(0).getStatus()).isNotNull();
        }

        @Test
        @DisplayName("활동원 가져오기 테스트")
        void getAllParticipantsTest() {
            // given
            Member member = memberRepository.save(Member.builder().build());

            Member participant1 = memberRepository.save(Member.builder()
                            .name("문희조")
                            .loginId("gmlwh124")
                            .major(Major.컴퓨터SW)
                            .build());

            Member participant2 = memberRepository.save(Member.builder()
                    .name("희조문")
                    .loginId("hejow124")
                    .major(Major.컴퓨터SW)
                    .build());

            Activity activity = activityRepository.save(Project.builder()
                            .leader(member)
                            .season(LocalDateTime.now())
                            .build());

            MemberActivity memberActivity1 = MemberActivity.builder()
                    .member(participant1)
                    .activity(activity)
                    .build();

            MemberActivity memberActivity2 = MemberActivity.builder()
                    .member(participant2)
                    .activity(activity)
                    .build();

            memberActivityRepository.saveAll(Arrays.asList(memberActivity1, memberActivity2));

            // when
            List<ParticipantResponse> participantResponses = memberActivityRepository.getAllParticipantByActivityId(activity.getId());

            // then
            ParticipantResponse response1 = participantResponses.get(0);
            ParticipantResponse response2 = participantResponses.get(1);

            assertThat(participantResponses.size()).isEqualTo(2);
            assertThat(response1.getName()).isNotNull();
            assertThat(response1.getLoginId()).isNotNull();
            assertThat(response1.getMajor()).isNotNull();
            assertThat(response2.getName()).isNotNull();
            assertThat(response2.getLoginId()).isNotNull();
            assertThat(response2.getMajor()).isNotNull();
        }

        @Test
        @DisplayName("활동 구성원 체크 테스트")
        void existMemberOfActivity() {
            // given
            String loginId = "gmlwh124";

            Member member = memberRepository.save(Member.builder()
                            .loginId(loginId)
                            .build());

            Activity activity = activityRepository.save(Study.builder()
                    .season(LocalDateTime.now())
                    .build());

            memberActivityRepository.save(MemberActivity.builder()
                    .member(member)
                    .activity(activity)
                    .build());

            // when
            Member foundMember = memberActivityRepository.findMemberOfActivityByLoginId(activity.getId(), loginId);

            // then
            assertThat(foundMember).isNotNull();
            assertThat(foundMember.getLoginId()).isEqualTo(loginId);
        }
    }
}
