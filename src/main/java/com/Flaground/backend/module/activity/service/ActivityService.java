package com.Flaground.backend.module.activity.service;

import com.Flaground.backend.global.common.response.SearchResponse;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityDetailResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityResponse;
import com.Flaground.backend.module.activity.controller.dto.response.GetAllActivitiesResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipateResponse;
import com.Flaground.backend.module.activity.domain.Activity;
import com.Flaground.backend.module.activity.domain.ActivityData;
import com.Flaground.backend.module.activity.domain.repository.ActivityRepository;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        member.isWithdraw();
        return memberActivityService.getAllActivitiesOfMember(loginId);
    }

    @Transactional(readOnly = true)
    public List<ActivityApplyResponse> getAllApplies(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        return activityApplyService.getApplies(activity.getId());
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getAllParticipants(Long memberId, Long activityId) {
        validateLeaderAndReturnActivity(memberId, activityId);
        return memberActivityService.getAllParticipants(activityId);
    }

     @Transactional(readOnly = true)
    public SearchResponse<ActivityResponse> searchActivity(String keyword) {
         return activityRepository.searchActivity(keyword);
    }

    @Transactional(readOnly = true)
    public ActivityDetailResponse getDetails(Long activityId) {
        return activityRepository.getActivityDetail(activityId);
    }

    public boolean checkApply(Long memberId, Long activityId) {
        return activityApplyService.checkApply(memberId, activityId);
    }

    public void applyActivity(Long memberId, Long activityId) {
        isApplied(memberId, activityId);
        Member member = memberService.findById(memberId);
        activityApplyService.apply(member, activityId);
    }

    public void cancelApply(Long memberId, Long activityId) {
        isNotApplied(memberId, activityId);
        activityApplyService.cancelApply(memberId, activityId);
    }

    public Long create(Long memberId, ActivityData activityData) {
        Member member = memberService.findById(memberId);
        Activity activity = activityRepository.save(Activity.of(member, activityData));
        return activity.getId();
    }

    public void update(Long memberId, Long activityId, ActivityData activityData) {
        Activity savedActivity = validateLeaderAndReturnActivity(memberId, activityId);
        savedActivity.update(activityData);
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
        memberActivityService.deleteAll(activityId);
        activityRepository.delete(activity);
    }

    public void cleanActivities(Long memberId) {
        activityApplyService.deleteAllAppliesOfMember(memberId);
        memberActivityService.deleteAllOfMember(memberId);
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
        activity.validateLeader(memberId);
        return activity;
    }
}
