package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentoringService implements ActivityService {
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
    public void deleteActivity() {

    }
}
