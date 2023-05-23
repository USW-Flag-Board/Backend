package com.Flaground.backend.module.activity.domain.repository;

import com.Flaground.backend.global.common.SearchResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ActivityResponse;

import java.util.List;

public interface ActivityRepositoryCustom {
    List<ActivityResponse> getAllActivities();
    List<ActivityResponse> getRecruitActivities();
    SearchResponse<ActivityResponse> searchActivity(String keyword);
}
