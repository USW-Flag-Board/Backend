package com.Flaground.backend.module.activity.domain.repository;

import com.Flaground.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipateResponse;

import java.util.List;

public interface MemberActivityRepositoryCustom {
    List<ParticipateResponse> getActivitiesByLoginId(String loginId);

    List<ParticipantResponse> getParticipantOfActivity(Long activityId);
}
