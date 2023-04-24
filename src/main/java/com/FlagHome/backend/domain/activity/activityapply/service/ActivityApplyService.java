package com.FlagHome.backend.domain.activity.activityapply.service;

import com.FlagHome.backend.domain.activity.controller.dto.response.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityapply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.member.entity.Member;
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
