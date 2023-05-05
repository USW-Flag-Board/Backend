package com.FlagHome.backend.module.activity.activityapply.repository;

import com.FlagHome.backend.module.activity.activityapply.entity.ActivityApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityApplyRepository extends JpaRepository<ActivityApply, Long>, ActivityApplyRepositoryCustom {
}
