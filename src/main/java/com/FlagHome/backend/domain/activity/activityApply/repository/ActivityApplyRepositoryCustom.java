package com.FlagHome.backend.domain.activity.activityApply.repository;

import com.FlagHome.backend.domain.activity.activityApply.dto.ActivityApplyResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityApplyRepositoryCustom {
    List<ActivityApplyResponse> getAllApplies(long activityId);

    void deleteAllApplies(long activityId);
}
