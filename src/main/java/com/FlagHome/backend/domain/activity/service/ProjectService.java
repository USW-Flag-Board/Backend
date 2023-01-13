package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.activityApply.service.ActivityApplyService;
import com.FlagHome.backend.domain.activity.dto.CreateActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService implements ActivityService {
    private final ActivityRepository activityRepository;

    private final ActivityApplyService activityApplyService;

    private final MemberRepository memberRepository;

    @Transactional
    public Activity create(long memberId, CreateActivityRequest createActivityRequest) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Activity activity = activityRepository.save(Project.of(leader, createActivityRequest));

        return activity;
    }

    @Override
    public void delete(long activityId) {
        activityApplyService.deleteAllApplies(activityId);
        activityRepository.deleteById(activityId);
    }
}