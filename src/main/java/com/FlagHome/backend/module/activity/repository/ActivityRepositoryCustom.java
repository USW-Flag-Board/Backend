package com.FlagHome.backend.module.activity.repository;

import com.FlagHome.backend.module.activity.controller.dto.response.ActivityResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepositoryCustom {
    List<ActivityResponse> getAllActivities();
    List<ActivityResponse> getRecruitActivities();
}
