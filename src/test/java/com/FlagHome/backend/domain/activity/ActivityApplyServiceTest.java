package com.FlagHome.backend.domain.activity;

import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityapply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.activityapply.service.ActivityApplyService;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Study;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ActivityApplyServiceTest {
    @InjectMocks
    private ActivityApplyService activityApplyService;

    @Mock
    private ActivityApplyRepository activityApplyRepository;

    @Test
    @DisplayName("활동 신청여부 확인 테스트")
    void checkApplyTest() {
        // given
        long testId = 1L;
        given(activityApplyRepository.checkApply(eq(testId), eq(testId))).willReturn(true);

        // when
        boolean check = activityApplyService.checkApply(testId, testId);

        // then
        then(activityApplyRepository).should(times(1)).checkApply(testId, testId);
        assertThat(check).isTrue();
    }

    @Test
    @DisplayName("활동 신청하기 테스트")
    void applyActivityTest() {
        // given
        Long testId = 1L;
        Activity activity = Study.builder().build();
        ActivityApply apply = ActivityApply.builder().build();

        given(activityApplyRepository.save(any())).willReturn(apply);

        // when
        activityApplyService.apply(testId, activity);

        // then
        then(activityApplyRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("모든 활동 신청 가져오기 테스트")
    void getAllAppliesTest() {
        // given
        long testId = 1L;
        List<ActivityApplyResponse> activityApplyResponses = new ArrayList<>();
        given(activityApplyRepository.getAllApplies(eq(testId))).willReturn(activityApplyResponses);

        // when
        activityApplyService.getAllApplies(testId);

        // then
        then(activityApplyRepository).should(times(1)).getAllApplies(testId);
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
    void cancelApplyTesT() {
        // given
        long testId = 1L;
        ActivityApply apply = ActivityApply.builder().build();
        given(activityApplyRepository.findByMemberIdAndActivityId(eq(testId), eq(testId))).willReturn(apply);
        doNothing().when(activityApplyRepository).delete(any());

        // when
        activityApplyService.cancelApply(testId, testId);

        // then
        then(activityApplyRepository).should(times(1)).findByMemberIdAndActivityId(testId, testId);
        then(activityApplyRepository).should(times(1)).delete(any());
    }
}
