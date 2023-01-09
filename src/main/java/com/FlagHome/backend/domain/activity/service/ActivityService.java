package com.FlagHome.backend.domain.activity.service;

import org.springframework.transaction.annotation.Transactional;

public interface ActivityService {
    void createActivity();
    void getActivity();
    void updateActivity();

    @Transactional
    void deleteActivity(long activityId);
}
