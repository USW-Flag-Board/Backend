package com.FlagHome.backend.domain.activity.memberactivity.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberActivityRepositoryCustom {
    void deleteAllByActivityId(long activityId);
}
