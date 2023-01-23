package com.FlagHome.backend.domain.activity.activityApply.repository;

import com.FlagHome.backend.domain.activity.activityApply.dto.ActivityApplyResponse;
import com.FlagHome.backend.domain.activity.activityApply.entity.ActivityApply;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityApplyRepositoryCustom {
    List<ActivityApplyResponse> getAllApplies(long activityId);

    void deleteAllApplies(long activityId);

    boolean checkApply(long memberId, long activityId);

    ActivityApply findByMemberIdAndActivityId(long memberId, long activityId);
}
