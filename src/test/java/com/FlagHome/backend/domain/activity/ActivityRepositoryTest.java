package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.activityApply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.entity.*;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.config.QueryDslConfig;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static com.FlagHome.backend.domain.activity.entity.QMentoring.mentoring;
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
    void getAllActivitiesTest() {
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
}
