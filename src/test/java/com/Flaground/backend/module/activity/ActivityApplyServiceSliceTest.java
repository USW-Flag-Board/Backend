package com.Flaground.backend.module.activity;

import com.Flaground.backend.common.MockServiceTest;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.Flaground.backend.module.activity.activityapply.entity.ActivityApply;
import com.Flaground.backend.module.activity.activityapply.repository.ActivityApplyRepository;
import com.Flaground.backend.module.activity.activityapply.service.ActivityApplyService;
import com.Flaground.backend.module.activity.entity.Activity;
import com.Flaground.backend.module.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

public class ActivityApplyServiceSliceTest extends MockServiceTest {
    @InjectMocks
    private ActivityApplyService activityApplyService;

    @Mock
    private ActivityApplyRepository activityApplyRepository;

    @Test
    @DisplayName("활동 신청여부 확인 테스트")
    void checkApplyTest() {
        // given
        long testId = 1L;
        given(activityApplyRepository.isApplied(anyLong(), anyLong())).willReturn(true);

        // when
        boolean check = activityApplyService.checkApply(testId, testId);

        // then
        then(activityApplyRepository).should(times(1)).isApplied(anyLong(), anyLong());
        assertThat(check).isTrue();
    }

    @Test
    @DisplayName("활동 신청하기 테스트")
    void applyActivityTest() {
        // given
        Activity activity = Activity.builder().build();
        ActivityApply apply = ActivityApply.builder().build();
        Member member = Member.builder().build();

        given(activityApplyRepository.save(any())).willReturn(apply);

        // when
        activityApplyService.apply(member, activity);

        // then
        then(activityApplyRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("모든 활동 신청 가져오기 테스트")
    void getAllAppliesTest() {
        // given
        long testId = 1L;
        List<ActivityApplyResponse> activityApplyResponses = new ArrayList<>();
        given(activityApplyRepository.getAllApplies(anyLong())).willReturn(activityApplyResponses);

        // when
        activityApplyService.getAllApplies(testId);

        // then
        then(activityApplyRepository).should(times(1)).getAllApplies(anyLong());
    }

    @Test
    @DisplayName("모든 활동 신청 삭제하기 테스트")
    void deleteAllAppliesTest() {
        // given
        long testId = 1L;
        doNothing().when(activityApplyRepository).deleteAllApplies(testId);

        // then
        activityApplyService.deleteAllApplies(testId);

        // when
        then(activityApplyRepository).should(times(1)).deleteAllApplies(testId);
    }

    @Test
    @DisplayName("활동 신청 취소하기 테스트")
    void cancelApplyTest() {
        // given
        long testId = 1L;
        doNothing().when(activityApplyRepository).deleteByMemberIdAndActivityId(anyLong(), anyLong());

        // when
        activityApplyService.cancelApply(testId, testId);

        // then
        then(activityApplyRepository).should(times(1)).deleteByMemberIdAndActivityId(anyLong(), anyLong());
    }
}
