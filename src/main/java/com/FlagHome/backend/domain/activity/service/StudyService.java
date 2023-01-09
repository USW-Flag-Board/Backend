package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService implements ActivityService {
    private final ActivityRepository activityRepository;

    @Override
    public void createActivity() {

    }

    @Override
    public void getActivity() {

    }

    @Override
    public void updateActivity() {

    }

    @Override
    @Transactional
    public void deleteActivity(long activityId) {
        activityRepository.deleteById(activityId);
    }
}
