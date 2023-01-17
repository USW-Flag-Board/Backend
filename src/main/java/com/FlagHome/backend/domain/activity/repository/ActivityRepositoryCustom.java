package com.FlagHome.backend.domain.activity.repository;

import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepositoryCustom {
    ActivityResponse getActivity(long activityId);
    List<ActivityResponse> getAllActivities();
}
