package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.activityApply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityApply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.activityApply.service.ActivityApplyService;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Transactional
public class ActivityServiceTest {
    @InjectMocks
    private ActivityService activityService;

    @Mock
    private ActivityApplyService activityApplyService;

    @Mock
    private ActivityApplyRepository activityApplyRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("활동 신청 테스트")
    class activityApplyTest {
        @Test
        @DisplayName("활동 신청하기 테스트")
        void applyActivityTest() {
//            // given
//            Member member = Member.builder().id(1L).build();
//            Activity activity = Mentoring.builder().id(1L).leader(member).build();
//            ActivityApply activityApply = ActivityApply.builder().member(member).activity(activity).build();
//
//            given(activityApplyRepository.checkApply(any(), any())).willReturn(false);
//            given(activityApplyRepository.findById(any())).willReturn(Optional.ofNullable(activityApply));
//            given(activityRepository.findById(any())).willReturn(Optional.ofNullable(activity));
//
//            // when
//            activityService.applyActivity(member.getId(), activity.getId());
//
//            // then
        }
    }

    @Test
    @DisplayName("활동 만들기 테스트")
    void createActivityTest() {

    }
}
