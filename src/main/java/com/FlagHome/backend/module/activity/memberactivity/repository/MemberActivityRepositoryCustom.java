package com.FlagHome.backend.module.activity.memberactivity.repository;

import com.FlagHome.backend.module.activity.controller.dto.response.ParticipantResponse;
import com.FlagHome.backend.module.activity.controller.dto.response.ParticipateResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberActivityRepositoryCustom {
    List<ParticipateResponse> getAllActivitiesOfMember(String loginId);

    List<ParticipantResponse> getAllParticipantByActivityId(Long activityId);
}
