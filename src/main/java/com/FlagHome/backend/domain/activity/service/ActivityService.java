package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityapply.service.ActivityApplyService;
import com.FlagHome.backend.domain.activity.controller.dto.request.ActivityRequest;
import com.FlagHome.backend.domain.activity.controller.dto.response.ActivityResponse;
import com.FlagHome.backend.domain.activity.controller.dto.response.GetAllActivitiesResponse;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipantResponse;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.activity.memberactivity.service.MemberActivityService;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
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
        activity.changeLeader(member);
        return activityRepository.save(activity);
    }

    public void updateMentoring(Long memberId, Long activityId, ActivityRequest activityRequest) {
        Mentoring mentoring = (Mentoring) validateLeaderAndReturnActivity(memberId, activityId);
        mentoring.updateMentoring(activityRequest);
    }

    public void updateProject(Long memberId, Long activityId, ActivityRequest activityRequest) {
        Project project = (Project) validateLeaderAndReturnActivity(memberId, activityId);
        project.updateProject(activityRequest);
    }

    public void updateStudy(Long memberId, Long activityId, ActivityRequest activityRequest) {
        Study study = (Study) validateLeaderAndReturnActivity(memberId, activityId);
        study.updateStudy(activityRequest);
    }

    public void closeRecruitment(Long memberId, Long activityId, List<String> loginIdList) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        activity.isRecruitment();
        List<Member> memberList = memberService.getMembersByLoginIds(loginIdList);
        activityApplyService.deleteAllApplies(activityId);
        memberActivityService.registerMembers(activity, memberList);
        activity.closeRecruitment();
    }

    public void finishActivity(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        activity.isOn();
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