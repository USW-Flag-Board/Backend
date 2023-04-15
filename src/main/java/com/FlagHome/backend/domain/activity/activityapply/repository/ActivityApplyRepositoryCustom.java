package com.FlagHome.backend.domain.activity.activityapply.repository;

import com.FlagHome.backend.domain.activity.activityapply.dto.ActivityApplyResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityApplyRepositoryCustom {
    List<ActivityApplyResponse> getAllApplies(long activityId);

    void deleteAllApplies(long activityId);

    boolean isApplied(long memberId, long activityId);

    void deleteByMemberIdAndActivityId(long memberId, long activityId);
}
