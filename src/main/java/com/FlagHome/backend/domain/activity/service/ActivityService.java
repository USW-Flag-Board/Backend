package com.FlagHome.backend.domain.activity.service;

import com.FlagHome.backend.domain.activity.activityApply.service.ActivityApplyService;
import com.FlagHome.backend.domain.activity.dto.ActivityRequest;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface ActivityService {
    void update(long activityId, ActivityRequest activityRequest);

    @Transactional
    default Activity create(Activity activity, ActivityRepository activityRepository) {
        return activityRepository.save(activity);
    }

    @Transactional
    default void delete(long activityId, ActivityRepository activityRepository, ActivityApplyService activityApplyService) {
//        if (!activityRepository.existsById(activityId)) {
//            throw new CustomException(ErrorCode.ACTIVITY_NOT_FOUND);
//        }
//        activityApplyService.deleteAllApplies(activityId);
//        activityRepository.deleteById(activityId);
    }
}