package com.FlagHome.backend.domain.activity.repository;

import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRepositoryCustom {
    Optional<ActivityResponse> getActivity(long activityId);
    List<ActivityResponse> getAllActivities();
}
