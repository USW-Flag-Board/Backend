package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.entity.enums.ActivityType;
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
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityApplyService activityApplyService;
    private final MemberActivityService memberActivityService;
    private final MemberService memberService;

    public ActivityResponse getActivity(Long activityId) {
        return activityRepository.getActivity(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }

    @Transactional(readOnly = true) // 수정 요망
    public GetAllActivitiesResponse getAllActivities() {
        List<ActivityResponse> activityResponseList = activityRepository.getAllActivities();

        Map<String, Map<ActivityType, List<ActivityResponse>>> allActivities = activityResponseList.stream()
                .collect(groupingBy(ActivityResponse::getYear,
                        groupingBy(ActivityResponse::getActivityType, toList())));

        return GetAllActivitiesResponse.from(allActivities);
    }

    @Transactional
    public List<ActivityApplyResponse> getAllActivityApplies(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        return activityApplyService.getAllApplies(activity.getId());
    }

    @Transactional
    public List<ParticipantResponse> getAllParticipants(Long memberId, Long activityId) {
        validateLeaderAndReturnActivity(memberId, activityId);
        return memberActivityService.getAllParticipants(activityId);
    }

    @Transactional(readOnly = true)
    public List<ParticipateResponse> getMemberPageActivities(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        member.isAvailable();
        return memberActivityService.getAllActivitiesOfMember(loginId);
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getRecruitActivities() {
        return activityRepository.getRecruitActivities();
    }

    public Boolean checkApply(Long memberId, Long activityId) {
        return activityApplyService.checkApply(memberId, activityId);
    }

    @Transactional
    public ActivityApply applyActivity(Long memberId, Long activityId) {
        if (checkApply(memberId, activityId)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED);
        }
        Member member = memberService.findById(memberId);
        Activity activity = findById(activityId);
        return activityApplyService.apply(member, activity);
    }

    @Transactional
    public Activity create(Long memberId, Activity activity) {
        Member member = memberService.findById(memberId);
        activity.changeLeader(member);
        return activityRepository.save(activity);
    }

    @Transactional
    public void updateMentoring(Long memberId, Long activityId, ActivityRequest activityRequest) {
        Mentoring mentoring = (Mentoring) validateLeaderAndReturnActivity(memberId, activityId);
        mentoring.updateMentoring(activityRequest);
    }

    @Transactional
    public void updateProject(Long memberId, Long activityId, ActivityRequest activityRequest) {
        Project project = (Project) validateLeaderAndReturnActivity(memberId, activityId);
        project.updateProject(activityRequest);
    }

    @Transactional
    public void updateStudy(Long memberId, Long activityId, ActivityRequest activityRequest) {
        Study study = (Study) validateLeaderAndReturnActivity(memberId, activityId);
        study.updateStudy(activityRequest);
    }

    @Transactional
    public void changeLeader(Long memberId, Long activityId, String loginId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        Member newLeader = memberActivityService.findMemberOfActivity(activityId, loginId);
        if (newLeader == null) {
            throw new CustomException(ErrorCode.NOT_ACTIVITY_MEMBER);
        }

        activity.changeLeader(newLeader);
    }

    @Transactional
    public void closeRecruitment(Long memberId, Long activityId, List<String> loginIdList) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        List<Member> memberList = memberService.getMembersByLoginIds(loginIdList);
        activityApplyService.deleteAllApplies(activityId);
        memberActivityService.registerMembers(activity, memberList);
        activity.closeRecruitment();
    }

    @Transactional
    public void reopenRecruitment(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        memberActivityService.deleteAllByActivity(activity.getId());
        activity.reopenRecruitment();
    }

    @Transactional
    public void finishActivity(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);
        activity.finishActivity();
    }

    @Transactional
    public void delete(Long memberId, Long activityId) {
        Activity activity = validateLeaderAndReturnActivity(memberId, activityId);

        activityApplyService.deleteAllApplies(activityId);
        activityRepository.delete(activity);
    }

    @Transactional
    public void cancelApply(Long memberId, Long activityId) {
        if (!checkApply(memberId, activityId)) {
            throw new CustomException(ErrorCode.APPLY_NOT_FOUND);
        }
        activityApplyService.cancelApply(memberId, activityId);
    }

    private Activity validateLeaderAndReturnActivity(Long memberId, Long activityId) {
        Activity activity = findById(activityId);

        if (!Objects.equals(memberId, activity.getLeader().getId())) {
            throw new CustomException(ErrorCode.NOT_ACTIVITY_LEADER);
        }

        return activity;
    }

    private Activity findById(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }
}