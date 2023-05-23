package com.Flaground.backend.module.activity.service;

import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.Flaground.backend.module.activity.domain.ActivityApply;
import com.Flaground.backend.module.activity.domain.repository.ActivityApplyRepository;
import com.Flaground.backend.module.activity.domain.Activity;
import com.Flaground.backend.module.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityApplyService {
    private final ActivityApplyRepository activityApplyRepository;

    @Transactional(readOnly = true)
    public List<ActivityApplyResponse> getAllApplies(Long activityId) {
        return activityApplyRepository.getAllApplies(activityId);
    }

    public Boolean checkApply(Long memberId, Long activityId) {
        return activityApplyRepository.isApplied(memberId, activityId);
    }

    public ActivityApply apply(Member member, Activity activity) {
        ActivityApply apply = ActivityApply.of(member, activity);
        return activityApplyRepository.save(apply);
    }

    public void cancelApply(Long memberId, Long activityId) {
        activityApplyRepository.deleteByMemberIdAndActivityId(memberId, activityId);
    }

    public void deleteAllApplies(Long activityId) {
        activityApplyRepository.deleteAllApplies(activityId);
    }
}
