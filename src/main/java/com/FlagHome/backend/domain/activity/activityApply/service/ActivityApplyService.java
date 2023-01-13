package com.FlagHome.backend.domain.activity.activityApply.service;

import com.FlagHome.backend.domain.activity.activityApply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityApply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityApply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityApplyService {
    private final ActivityApplyRepository activityApplyRepository;

    // 권한 처리는 activityService 쪽에서
    @Transactional
    public void apply(Member member, Activity activity) {
        ActivityApply activityApply = ActivityApply.builder()
                .member(member)
                .activity(activity)
                .applyTime(LocalDateTime.now())
                .build();

        activityApplyRepository.save(activityApply);
    }

    @Transactional(readOnly = true)
    public List<ActivityApplyResponse> getApplies(long activityId) {
        return activityApplyRepository.getAllApplies(activityId);
    }

    @Transactional
    public void delete(long memberId) {
        ActivityApply activityApply = activityApplyRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLY_NOT_FOUND));

        activityApplyRepository.delete(activityApply);
    }

    @Transactional
    public void deleteAllApplies(long activityId) {
        ActivityApply activityApply = activityApplyRepository.findByActivityId(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.APPLY_NOT_FOUND));

        activityApplyRepository.deleteAllApplies(activityId);
    }
}
