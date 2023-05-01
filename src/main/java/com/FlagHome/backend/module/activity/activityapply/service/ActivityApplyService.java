package com.FlagHome.backend.module.activity.activityapply.service;

import com.FlagHome.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.FlagHome.backend.module.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.module.activity.activityapply.repository.ActivityApplyRepository;
import com.FlagHome.backend.module.activity.entity.Activity;
import com.FlagHome.backend.module.member.domain.Member;
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
