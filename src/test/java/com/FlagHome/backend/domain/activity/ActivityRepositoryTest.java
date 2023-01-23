package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.activityApply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityApply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityApply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.entity.*;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QueryDslConfig.class)
public class ActivityRepositoryTest {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityApplyRepository activityApplyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("활동 가져오기 테스트")
    public void getActivityTest() {
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
                .build();

        Activity activity = activityRepository.saveAndFlush(project);

        // when
        ActivityResponse activityResponse = activityRepository.getActivity(activity.getId());

        // then
        assertThat(activity.getId()).isEqualTo(activityResponse.getId());
        assertThat(activity.getName()).isEqualTo(activityResponse.getName());
        assertThat(activity.getLeader().getName()).isEqualTo(activityResponse.getLeader());
        assertThat(activityResponse.getActivityType()).isEqualTo(activityType);
    }

    @Test
    @DisplayName("모든 활동 가져오기 테스트")
    public void getAllActivitiesTest() {
        // given
        Member member = memberRepository.save(Member.builder().build());

        Project project = Project.builder()
                .leader(member)
                .activityType(ActivityType.PROJECT)
                .build();

        Study study = Study.builder()
                .leader(member)
                .activityType(ActivityType.STUDY)
                .build();

        Mentoring mentoring = Mentoring.builder()
                .leader(member)
                .activityType(ActivityType.MENTORING)
                .build();

        activityRepository.saveAll(Arrays.asList(project, study, mentoring));

        // when
        List<ActivityResponse> activityResponseList = activityRepository.getAllActivities();

        // then
        assertThat(activityResponseList.size()).isEqualTo(3);
    }

    @Nested
    @DisplayName("활동 신청 테스트")
    public class activityApplyTest {
        @Test
        @DisplayName("모든 활동 신청 가져오기 테스트")
        public void getAllAplliesTest() {
            // given
            Major major = Major.컴퓨터SW;

            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().major(major).build());
            Member member3 = memberRepository.save(Member.builder().major(major).build());

            Activity activity = activityRepository.saveAndFlush(Project.builder().leader(member1).build());

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
        public void deleteAllAppliesTest() {
            // given
            Major major = Major.컴퓨터SW;

            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().major(major).build());
            Member member3 = memberRepository.save(Member.builder().major(major).build());

            Activity activity = activityRepository.saveAndFlush(Project.builder().leader(member1).build());

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
        public void checkApplyTest() {
            // given
            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().build());

            Activity activity = activityRepository.save(Study.builder().leader(member1).build());

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
        @DisplayName("활동 신청 정보 가져오기 테스트")
        public void findApplyByMemberAndActivityTest() {
            // given
            Member member = memberRepository.save(Member.builder().build());
            Activity activity = activityRepository.save(Mentoring.builder().leader(member).build());

            ActivityApply apply = activityApplyRepository.save(ActivityApply.builder()
                    .member(member)
                    .activity(activity)
                    .build());

            // when
            ActivityApply findApply = activityApplyRepository.findByMemberIdAndActivityId(member.getId(), activity.getId());

            // then
            assertThat(apply.getId()).isEqualTo(findApply.getId());
            assertThat(apply.getMember()).isEqualTo(findApply.getMember());
            assertThat(apply.getActivity()).isEqualTo(findApply.getActivity());
        }
    }
}
