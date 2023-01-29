package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.ActivityType;
import com.FlagHome.backend.domain.activity.Status;
import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityapply.entity.ActivityApply;
import com.FlagHome.backend.domain.activity.activityapply.service.ActivityApplyService;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.dto.GetAllActivitiesResponse;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.activity.memberactivity.service.MemberActivityService;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityApplyService activityApplyService;
    private final MemberActivityService memberActivityService;
    private final MemberService memberService;

    public ActivityResponse getActivity(long activityId) {
        if (!activityRepository.existsById(activityId)) {
            throw new CustomException(ErrorCode.ACTIVITY_NOT_FOUND);
        }

        return activityRepository.getActivity(activityId);
    }

    @Transactional(readOnly = true)
    public GetAllActivitiesResponse getAllActivities() {
        List<ActivityResponse> activityResponseList = activityRepository.getAllActivities();

        Map<String, Map<ActivityType, List<ActivityResponse>>> allActivities = activityResponseList.stream()
                .collect(groupingBy(ActivityResponse::getYear,
                        groupingBy(ActivityResponse::getActivityType, toList())));

        return GetAllActivitiesResponse.from(allActivities);
    }

    @Transactional(readOnly = true)
    public List<ActivityApplyResponse> getAllActivityApplies(long memberId, long activityId) {
        Activity activity = validateLeaderAndReturn(memberId, activityId);
        return activityApplyService.getAllApplies(activity.getId());
    }

    public boolean checkApply(long memberId, long activityId) {
        return activityApplyService.checkApply(memberId, activityId);
    }

    @Transactional
    public ActivityApply applyActivity(long memberId, long activityId) {
        if (checkApply(memberId, activityId)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED);
        }
        Activity activity = findById(activityId);
        return activityApplyService.apply(memberId, activity);
    }

    @Transactional
    public Activity create(long memberId, ActivityRequest activityRequest) {
        Activity activity = Arrays.stream(ActivityType.values())
                .filter(type -> type == activityRequest.getActivityType())
                .findFirst()
                .map(type -> type.toEntity(activityRequest))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_SUPPORT_ACTIVITY));

        Member member = Member.builder().id(memberId).build();
        activity.setLeader(member);

        return activityRepository.save(activity);
    }

    @Transactional
    public void updateMentoring(long memberId, long activityId, ActivityRequest activityRequest) {
        Mentoring mentoring = (Mentoring) validateLeaderAndReturn(memberId, activityId);
        mentoring.updateMentoring(activityRequest);
    }

    @Transactional
    public void updateProject(long memberId, long activityId, ActivityRequest activityRequest) {
        Project project = (Project) validateLeaderAndReturn(memberId, activityId);
        project.updateProject(activityRequest);
    }

    @Transactional
    public void updateStudy(long memberId, long activityId, ActivityRequest activityRequest) {
        Study study = (Study) validateLeaderAndReturn(memberId, activityId);
        study.updateStudy(activityRequest);
    }

    @Transactional
    public void changeLeader(long memberId, long activityId, String loginId) {
        Activity activity = validateLeaderAndReturn(memberId, activityId);
        Member newLeader = memberService.findByLoginId(loginId);

        activity.setLeader(newLeader);
    }

    @Transactional
    public void closeRecruitment(long memberId, long activityId, List<String> loginIdList) {
        Activity activity = validateLeaderAndReturn(memberId, activityId);
        List<Member> memberList = memberService.getMembersByLoginId(loginIdList);

        memberActivityService.registerMembers(activity, memberList);
        activity.updateStatus(Status.ON); // 다시 모집할 수 있으므로 신청내역은 남겨둔다.
    }

    @Transactional
    public void reopenRecruitment(long memberId, long activityId) {
        Activity activity = validateLeaderAndReturn(memberId, activityId);

        memberActivityService.deleteAllByActivity(activity.getId());
        activity.updateStatus(Status.RECRUIT);
    }

    @Transactional
    public void finishActivity(long memberId, long activityId) {
        Activity activity = validateLeaderAndReturn(memberId, activityId);
        activity.updateStatus(Status.OFF);
    }

    @Transactional
    public void delete(long memberId, long activityId) {
        Activity activity = validateLeaderAndReturn(memberId, activityId);

        activityApplyService.deleteAllApplies(activityId);
        activityRepository.delete(activity);
    }

    @Transactional
    public void cancelApply(long memberId, long activityId) {
        if (!checkApply(memberId, activityId)) {
            throw new CustomException(ErrorCode.APPLY_NOT_FOUND);
        }
        activityApplyService.cancelApply(memberId, activityId);
    }

    private Activity validateLeaderAndReturn(long memberId, long activityId) {
        Activity activity = findById(activityId);

        if (memberId != activity.getLeader().getId()) {
            throw new CustomException(ErrorCode.NOT_ACTIVITY_LEADER);
        }

        return activity;
    }

    private Activity findById(long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));
    }
}