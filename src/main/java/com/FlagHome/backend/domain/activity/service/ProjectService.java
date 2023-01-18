package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.entity.Project;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("Project")
@RequiredArgsConstructor
public class ProjectService implements ActivityService {
    private final ActivityRepository activityRepository;

    @Override
    public void update(long activityId, ActivityRequest activityRequest) {
        Project project = (Project) activityRepository.findById(activityId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_NOT_FOUND));

        project.updateProject(activityRequest);
    }
}
