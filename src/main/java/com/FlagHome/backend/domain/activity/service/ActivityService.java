package com.FlagHome.backend.domain.activity.service;


import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    private String calculatePeriod() {
        return null;
    }

    @Transactional
    public void deleteActivity(long activityId) {
        activityRepository.deleteById(activityId);
    }
}
