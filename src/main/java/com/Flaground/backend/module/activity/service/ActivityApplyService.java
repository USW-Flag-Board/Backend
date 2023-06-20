package com.Flaground.backend.module.activity.service;

import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.Flaground.backend.module.activity.domain.ActivityApply;
import com.Flaground.backend.module.activity.domain.repository.ActivityApplyRepository;
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
    public List<ActivityApplyResponse> getApplies(Long activityId) {
        return activityApplyRepository.getApplies(activityId);
    }

    public boolean checkApply(Long memberId, Long activityId) {
        return activityApplyRepository.isApplied(memberId, activityId);
    }

    public void apply(Member member, Long activityId) {
        ActivityApply apply = ActivityApply.of(member, activityId);
        activityApplyRepository.save(apply);
    }

    public void cancelApply(Long memberId, Long activityId) {
        activityApplyRepository.deleteByIds(memberId, activityId);
    }

    public void deleteAllApplies(Long activityId) {
        activityApplyRepository.deleteAll(activityId);
    }

    public void deleteAllAppliesOfMember(Long memberId) {
        activityApplyRepository.deleteAllOfMember(memberId);
    }
}
