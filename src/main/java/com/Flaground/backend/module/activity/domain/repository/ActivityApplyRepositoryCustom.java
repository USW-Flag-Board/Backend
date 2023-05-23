package com.Flaground.backend.module.activity.domain.repository;

import com.Flaground.backend.module.activity.controller.dto.response.ActivityApplyResponse;

import java.util.List;

public interface ActivityApplyRepositoryCustom {
    List<ActivityApplyResponse> getApplies(Long activityId);

    void deleteAll(Long activityId);

    boolean isApplied(Long memberId, Long activityId);

    void deleteByIds(Long memberId, Long activityId);
}
