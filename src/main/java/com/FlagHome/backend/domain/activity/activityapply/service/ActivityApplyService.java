package com.FlagHome.backend.domain.activity.activityapply.service;

import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityapply.repository.ActivityApplyRepository;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityApplyService {
    private final ActivityApplyRepository activityApplyRepository;

    public boolean checkApply(long memberId, long activityId) {
        return activityApplyRepository.checkApply(memberId, activityId);
    }

    @Transactional
    public ActivityApply apply(long memberId, Activity activity) {
        ActivityApply activityApply = ActivityApply.builder()
                .member(Member.builder().id(memberId).build())
                .activity(activity)
                .applyTime(LocalDateTime.now())
                .build();

        return activityApplyRepository.save(activityApply);
    }

    @Transactional(readOnly = true)
    public List<ActivityApplyResponse> getAllApplies(long activityId) {
        return activityApplyRepository.getAllApplies(activityId);
    }

    @Transactional
    public void cancelApply(long memberId, long activityId) {
        activityApplyRepository.deleteByMemberIdAndActivityId(memberId, activityId);
    }

    @Transactional
    public void deleteAllApplies(long activityId) {
        activityApplyRepository.deleteAllApplies(activityId);
    }
}
