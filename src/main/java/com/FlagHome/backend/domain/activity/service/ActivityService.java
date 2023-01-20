package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.activityApply.service.ActivityApplyService;
import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.dto.ChangeLeaderRequest;
import com.FlagHome.backend.domain.activity.dto.CloseActivityRequest;
import com.FlagHome.backend.domain.activity.dto.UpdateActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Mentoring;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.entity.Study;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityApplyService activityApplyService;
    private final MemberRepository memberRepository;

    public ActivityResponse getActivity(long activityId) {
        if (!activityRepository.existsById(activityId)) {
            throw new CustomException(ErrorCode.ACTIVITY_NOT_FOUND);
        }

        ActivityResponse activityResponse = activityRepository.getActivity(activityId);
        return activityResponse;
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getAllActivities() {
        List<ActivityResponse> activityResponseList = activityRepository.getAllActivities();
        return activityResponseList;
    }

    public boolean checkApply(long memberId, long activityId) {
        return activityApplyService.checkApply(memberId, activityId);
    }
    @Transactional
    public void applyActivity(long memberId, long activityId) {
        Activity activity = findById(activityId);
        activityApplyService.apply(memberId, activity);
    }

    @Transactional
    public long create(Activity activity) {
        Activity savedActivity = activityRepository.save(activity);
        return savedActivity.getId();
    }

    @Transactional
    public void updateMentoring(long memberId, UpdateActivityRequest updateActivityRequest) {
        Mentoring mentoring = (Mentoring) validateLeaderAndReturn(memberId, updateActivityRequest.getId());
        mentoring.updateMentoring(updateActivityRequest);
    }

    @Transactional
    public void updateProject(long memberId, UpdateActivityRequest updateActivityRequest) {
        Project project = (Project) validateLeaderAndReturn(memberId, updateActivityRequest.getId());
        project.updateProject(updateActivityRequest);
    }

    @Transactional
    public void updateStudy(long memberId, UpdateActivityRequest updateActivityRequest) {
        Study study = (Study) validateLeaderAndReturn(memberId, updateActivityRequest.getId());
        study.updateStudy(updateActivityRequest);
    }

    @Transactional
    public void changeLeader(long memberId, ChangeLeaderRequest changeLeaderRequest) {
        Activity activity = validateLeaderAndReturn(memberId, changeLeaderRequest.getId());

        Member newLeader = memberRepository.findByLoginId(changeLeaderRequest.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        activity.setLeader(newLeader);
    }

    @Transactional
    public void closeRecruitment(long memberId, CloseActivityRequest closeActivityRequest) {
        Activity activity = validateLeaderAndReturn(memberId, closeActivityRequest.getId());

        activityApplyService.deleteAllApplies(closeActivityRequest.getId());
        activity.closeRecruitment();
    }

    @Transactional
    public void delete(long memberId, long activityId) {
        Activity activity = validateLeaderAndReturn(memberId, activityId);

        activityApplyService.deleteAllApplies(activity.getId());
        activityRepository.delete(activity);
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