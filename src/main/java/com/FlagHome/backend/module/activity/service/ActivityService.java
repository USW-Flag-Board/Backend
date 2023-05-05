package com.FlagHome.backend.module.activity.service;

import com.FlagHome.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.FlagHome.backend.module.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.module.activity.activityapply.service.ActivityApplyService;
import com.FlagHome.backend.module.activity.controller.dto.response.ActivityResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.GetAllActivitiesResponse;
import com.FlagHome.backend.module.activity.entity.Activity;
import com.FlagHome.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.ParticipateResponse;
import com.FlagHome.backend.module.activity.memberactivity.service.MemberActivityService;
import com.FlagHome.backend.module.activity.repository.ActivityRepository;
import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.member.service.MemberService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityApplyService activityApplyService;
    private final MemberActivityService memberActivityService;
    private final MemberService memberService;

    @Transactional(readOnly = true)
    public GetAllActivitiesResponse getAllActivities() {
        List<ActivityResponse> activityResponseList = activityRepository.getAllActivities();
        return GetAllActivitiesResponse.from(activityResponseList);
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getRecruitActivities() {
        return activityRepository.getRecruitActivities();
    }

    @Transactional(readOnly = true)
    public List<ParticipateResponse> getMemberPageActivities(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        member.isAvailable();
        return memberActivityService.getAllActivitiesOfMember(loginId);
    }

    @Transactional(readOnly = true)
    public List<ActivityApplyResponse> getAllApplies(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        return activityApplyService.getAllApplies(activity.getId());
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getAllParticipants(Long memberId, Long activityId) {
        validateLeaderAndReturnActivity(memberId, activityId);
        return memberActivityService.getAllParticipants(activityId);
    }

    // todo : 활동 검색기능 구현하기

    public Activity getActivity(Long activityId) {
        return findById(activityId);
    }

    public Boolean checkApply(Long memberId, Long activityId) {
        return activityApplyService.checkApply(memberId, activityId);
    }

    public ActivityApply applyActivity(Long memberId, Long activityId) {
        isApplied(memberId, activityId);
        Member member = memberService.findById(memberId);
        Activity activity = findById(activityId);
        return activityApplyService.apply(member, activity);
    }

    public void cancelApply(Long memberId, Long activityId) {
        isNotApplied(memberId, activityId);
        activityApplyService.cancelApply(memberId, activityId);
    }

    public Activity create(Long memberId, Activity activity) {
        Member member = memberService.findById(memberId);
        return activityRepository.save(Activity.of(member, activity));
    }

    public void update(Long memberId, Long activityId, Activity activity) {
        Activity savedActivity = validateLeaderAndReturnActivity(memberId, activityId);
        savedActivity.update(activity);
    }

    public void closeRecruitment(Long memberId, Long activityId, List<String> loginIdList) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        activity.isRecruiting();
        List<Member> memberList = memberService.getMembersByLoginIds(loginIdList);
        activityApplyService.deleteAllApplies(activityId);
        memberActivityService.registerMembers(activity, memberList);
        activity.closeRecruitment();
    }

    public void finishActivity(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        activity.isInProgress();
        activity.finishActivity();
    }

    public void delete(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        activityApplyService.deleteAllApplies(activityId);
        activityRepository.delete(activity);
    }

    private Activity findById(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }

    private void isApplied(Long memberId, Long activityId) {
        if (checkApply(memberId, activityId)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED);
        }
    }

    private void isNotApplied(Long memberId, Long activityId) {
        if (!checkApply(memberId, activityId)) {
            throw new CustomException(ErrorCode.APPLY_NOT_FOUND);
        }
    }

    private Activity validateLeaderAndReturnActivity(Long memberId, Long activityId) {
        Activity activity = findById(activityId);
        validateLeader(memberId, activity.getLeader().getId());
        return activity;
    }

    private void validateLeader(Long inputId, Long savedId) {
        if (!Objects.equals(inputId, savedId)) {
            throw new CustomException(ErrorCode.NOT_ACTIVITY_LEADER);
        }
    }
}