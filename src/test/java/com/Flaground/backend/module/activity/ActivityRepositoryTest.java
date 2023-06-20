package com.Flaground.backend.module.activity;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.global.common.response.SearchResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityDetailResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipateResponse;
import com.Flaground.backend.module.activity.domain.Activity;
import com.Flaground.backend.module.activity.domain.ActivityApply;
import com.Flaground.backend.module.activity.domain.ActivityInfo;
import com.Flaground.backend.module.activity.domain.MemberActivity;
import com.Flaground.backend.module.activity.domain.enums.ActivityType;
import com.Flaground.backend.module.activity.domain.enums.BookUsage;
import com.Flaground.backend.module.activity.domain.enums.Proceed;
import com.Flaground.backend.module.activity.domain.repository.ActivityApplyRepository;
import com.Flaground.backend.module.activity.domain.repository.ActivityRepository;
import com.Flaground.backend.module.activity.domain.repository.MemberActivityRepository;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityRepositoryTest extends RepositoryTest {
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
    class 활동_테스트 {
        @Test
        void 모든_활동_가져오기_테스트() {
            // given
            Member member = memberRepository.save(Member.builder().build());
            ActivityInfo info = ActivityInfo.builder().build();

            Activity project = Activity.builder().leader(member).type(ActivityType.PROJECT).info(info).build();
            Activity study = Activity.builder().leader(member).type(ActivityType.STUDY).info(info).build();
            Activity mentoring = Activity.builder().leader(member).type(ActivityType.MENTORING).info(info).build();

            activityRepository.saveAll(Arrays.asList(project, study, mentoring));

            // when
            List<ActivityResponse> activityResponseList = activityRepository.getAllActivities();

            // then
            assertThat(activityResponseList.size()).isEqualTo(3);
        }

        @Test
        void 활동_상세보기_테스트() {
            // given
            final String name = "john";
            final Proceed proceed = Proceed.BOTH;
            final BookUsage notUse = BookUsage.NOT_USE;
            final String empty = "";

            Member member = memberRepository.save(Member.builder().name(name).build());
            ActivityInfo info = ActivityInfo.builder().proceed(proceed).bookUsage(notUse).bookName(empty).githubURL(empty).build();
            Activity activity = Activity.builder().leader(member).info(info).build();
            activityRepository.save(activity);

            // when
            ActivityDetailResponse activityDetail = activityRepository.getActivityDetail(activity.getId());

            // then
            assertThat(activityDetail.getLeader()).isEqualTo(name);
            assertThat(activityDetail.getProceed()).isEqualTo(proceed);
            assertThat(activityDetail.getBookUsage()).isEqualTo(notUse);
            assertThat(activityDetail.getBookName()).isEqualTo(empty);
            assertThat(activityDetail.getGithubURL()).isEqualTo(empty);
            assertThat(activityDetail.getSemester()).isNotNull();
        }
    }

    @Nested
    class 활동신청_테스트 {
        @Test
        void 멤버가_신청한_모든_활동_지우기_테스트() {
            // given
            Member leader = memberRepository.save(Member.builder().build());
            Member applier = memberRepository.save(Member.builder().build());
            Activity activity = activityRepository.save(Activity.builder().leader(leader).build());
            activityApplyRepository.save(ActivityApply.of(applier, activity.getId()));

            // when
            activityApplyRepository.deleteAllOfMember(applier.getId());

            // then
            List<ActivityApply> applies = activityApplyRepository.findAll();
            assertThat(applies).isEmpty();
        }

        @Test
        void 모든_활동_신청_가져오기_테스트() {
            // given
            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().build());
            Member member3 = memberRepository.save(Member.builder().build());

            Activity activity = activityRepository.saveAndFlush(Activity.builder().leader(member1).build());

            ActivityApply activityApply1 = ActivityApply.builder().member(member2).activityId(activity.getId()).build();
            ActivityApply activityApply2 = ActivityApply.builder().member(member3).activityId(activity.getId()).build();

            activityApplyRepository.saveAll(Arrays.asList(activityApply1, activityApply2));

            // when
            List<ActivityApplyResponse> responses = activityApplyRepository.getApplies(activity.getId());

            // then
            assertThat(responses.size()).isEqualTo(2);
            assertThat(responses.get(0).getId()).isNotEqualTo(responses.get(1).getId());
        }

        @Test
        @DisplayName("모든 신청 삭제하기 테스트")
        void deleteAllAppliesTest() {
            // given
            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().build());
            Member member3 = memberRepository.save(Member.builder().build());

            Activity activity = activityRepository.saveAndFlush(Activity.builder().leader(member1).build());

            ActivityApply activityApply1 = ActivityApply.builder().member(member2).activityId(activity.getId()).build();
            ActivityApply activityApply2 = ActivityApply.builder().member(member3).activityId(activity.getId()).build();

            activityApplyRepository.saveAll(Arrays.asList(activityApply1, activityApply2));

            // when
            activityApplyRepository.deleteAll(activity.getId());
            List<ActivityApplyResponse> responses = activityApplyRepository.getApplies(activity.getId());

            // then
            assertThat(responses.size()).isEqualTo(0);
        }

        @Test
        void 신청_체크_테스트() {
            // given
            Member member1 = memberRepository.save(Member.builder().build());
            Member member2 = memberRepository.save(Member.builder().build());

            Activity activity = activityRepository.save(Activity.builder().leader(member1).build());

            activityApplyRepository.save(ActivityApply.of(member2, activity.getId()));

            // when
            boolean check1 = activityApplyRepository.isApplied(member1.getId(), activity.getId());
            boolean check2 = activityApplyRepository.isApplied(member2.getId(), activity.getId());

            // then
            assertThat(check1).isFalse();
            assertThat(check2).isTrue();
        }

        @Test
        void 활동_신청_정보_지우기_테스트() {
            // given
            Member member = memberRepository.save(Member.builder().build());
            Activity activity = activityRepository.save(Activity.builder().leader(member).build());

            ActivityApply apply = activityApplyRepository.save(ActivityApply.of(member, activity.getId()));

            // when
            entityManager.clear();
            activityApplyRepository.deleteByIds(member.getId(), activity.getId());

            // then
            Optional<ActivityApply> findApply = activityApplyRepository.findById(apply.getId());
            assertThat(findApply.isPresent()).isFalse();
        }
    }

    @Nested
    class 멤버활동_테스트 {
        @Test
        @DisplayName("주어진 멤버 아이디로 멤버활동이 모두 삭제되어야 한다")
        void deleteAllOfMemberTest() {
            // given
            Member leader = memberRepository.save(Member.builder().build());
            Member applier = memberRepository.save(Member.builder().build());
            Activity activity = activityRepository.save(Activity.builder().leader(leader).build());
            memberActivityRepository.save(MemberActivity.of(applier, activity));

            // when
            memberActivityRepository.deleteAllOfMember(applier.getId());

            // then
            List<MemberActivity> memberActivities = memberActivityRepository.findAll();
            assertThat(memberActivities).isEmpty();
        }

        @Test
        @DisplayName("멤버 참가활동 가져오기 테스트")
        void getAllParticipateActivityTest() {
            // given
            String loginId = "gmlwh124";
            Member member = memberRepository.save(Member.builder().loginId(loginId).build());
            ActivityInfo info = ActivityInfo.builder().build();

            Activity project = Activity.builder().type(ActivityType.PROJECT).info(info).build();
            Activity study = Activity.builder().type(ActivityType.STUDY).info(info).build();
            Activity mentoring = Activity.builder().type(ActivityType.MENTORING).info(info).build();

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
            List<ParticipateResponse> responseList = memberActivityRepository.getActivitiesByLoginId(loginId);

            // then
            assertThat(responseList.size()).isEqualTo(3);
            assertThat(responseList.get(0).getId()).isNotNull();
            assertThat(responseList.get(0).getYear()).isInstanceOf(Integer.class);
            assertThat(responseList.get(0).getSemester()).isNotNull();
            assertThat(responseList.get(0).getActivityStatus()).isNotNull();
        }

        @Test
        @DisplayName("활동원 가져오기 테스트")
        void getAllParticipantsTest() {
            // given
            Member member = memberRepository.save(Member.builder().build());

            Member participant1 = memberRepository.save(Member.builder()
                            .name("문희조")
                            .loginId("gmlwh124")
                            .build());

            Member participant2 = memberRepository.save(Member.builder()
                    .name("희조문")
                    .loginId("hejow124")
                    .build());

            Activity activity = activityRepository.save(Activity.builder()
                            .leader(member)
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
            List<ParticipantResponse> participantResponses = memberActivityRepository.getParticipantOfActivity(activity.getId());

            // then
            ParticipantResponse response1 = participantResponses.get(0);
            ParticipantResponse response2 = participantResponses.get(1);

            assertThat(participantResponses.size()).isEqualTo(2);
            assertThat(response1.getName()).isNotNull();
            assertThat(response1.getLoginId()).isNotNull();
            assertThat(response2.getName()).isNotNull();
            assertThat(response2.getLoginId()).isNotNull();
        }

        @Test
        void 멤버활동_지우기_테스트() {
            // given
            Member member = memberRepository.save(Member.builder().build());
            Activity activity = activityRepository.save(Activity.builder().build());
            memberActivityRepository.save(MemberActivity.of(member, activity));

            // when
            memberActivityRepository.deleteAllByActivity(activity.getId());

            // then
            List<MemberActivity> memberActivities = memberActivityRepository.findAll();
            assertThat(memberActivities).isEmpty();
        }
    }

    @Test
    void 모집_중인_활동_가져오기_테스트() {
        // given
        Member member = memberRepository.save(Member.builder().build());
        ActivityInfo info = ActivityInfo.builder().build();

        for (int i = 0; i < 5; i++) {
            activityRepository.save(Activity.builder().leader(member).info(info).build());
        }

        // when
        List<ActivityResponse> response = activityRepository.getRecruitActivities();

        // then
        assertThat(response).isNotNull();
        assertThat(response.size()).isEqualTo(3);
    }

    @Test
    void 활동_검색_테스트() {
        // given
        final String keyword = "keyword";
        Member member = memberRepository.save(Member.builder().build());
        ActivityInfo info = ActivityInfo.builder().build();

        Activity activity1 = Activity.builder().leader(member).name(keyword).description(keyword).info(info).build();
        Activity activity2 = Activity.builder().leader(member).description(keyword).info(info).build();

        activityRepository.saveAllAndFlush(List.of(activity1, activity2));

        // when
        SearchResponse<ActivityResponse> response = activityRepository.searchActivity(keyword);

        // then
        assertThat(response.getSearchResults()).isNotNull();
        assertThat(response.getResultCount()).isEqualTo(2);
    }
}
