package com.FlagHome.backend.domain.activity.memberactivity.repository;

import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberActivityRepositoryCustom {
    void deleteAllByActivityId(long activityId);

    List<ParticipateResponse> getAllActivitiesOfMember(String loginId);
}
