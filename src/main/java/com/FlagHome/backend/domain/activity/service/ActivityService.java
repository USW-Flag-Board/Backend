package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.dto.CreateActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ActivityService {
    Activity create(long memberId, CreateActivityRequest createActivityRequest);
    void delete(long activityId);
}