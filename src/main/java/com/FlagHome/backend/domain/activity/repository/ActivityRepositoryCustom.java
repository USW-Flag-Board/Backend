package com.FlagHome.backend.domain.activity.repository;

import com.FlagHome.backend.domain.activity.controller.dto.response.ActivityResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepositoryCustom {
    List<ActivityResponse> getAllActivities();
    List<ActivityResponse> getRecruitActivities();
}
