package com.Flaground.backend.module.activity.domain.repository;

import com.Flaground.backend.global.common.response.SearchResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityDetailResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityResponse;

import java.util.List;

public interface ActivityRepositoryCustom {
    ActivityDetailResponse getActivityDetail(Long activityId);
    List<ActivityResponse> getAllActivities();
    List<ActivityResponse> getRecruitActivities();
    SearchResponse<ActivityResponse> searchActivity(String keyword);
}
