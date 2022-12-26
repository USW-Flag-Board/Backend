package com.FlagHome.backend.domain.activity.repository;


import com.FlagHome.backend.domain.activity.entity.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepositoryCustom {
    List<Activity> findByActivityId(long activityId);
}
