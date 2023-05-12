package com.Flaground.backend.module.activity.memberactivity.repository;

import com.Flaground.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.Flaground.backend.module.activity.controller.dto.response.ParticipateResponse;

import java.util.List;

public interface MemberActivityRepositoryCustom {
    List<ParticipateResponse> getAllActivitiesOfMember(String loginId);

    List<ParticipantResponse> getAllParticipantByActivityId(Long activityId);
}
