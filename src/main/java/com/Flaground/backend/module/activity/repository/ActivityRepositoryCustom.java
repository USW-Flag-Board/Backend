package com.Flaground.backend.module.activity.repository;

import com.Flaground.backend.module.activity.controller.dto.response.ActivityResponse;

import java.util.List;

public interface ActivityRepositoryCustom {
    List<ActivityResponse> getAllActivities();
    List<ActivityResponse> getRecruitActivities();
}
