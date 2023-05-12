package com.Flaground.backend.module.activity.activityapply.repository;

import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;

import java.util.List;

public interface ActivityApplyRepositoryCustom {
    List<ActivityApplyResponse> getAllApplies(long activityId);

    void deleteAllApplies(long activityId);

    boolean isApplied(long memberId, long activityId);

    void deleteByMemberIdAndActivityId(long memberId, long activityId);
}
