package com.Flaground.backend.module.activity.activityapply.repository;

import com.Flaground.backend.module.activity.activityapply.entity.ActivityApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityApplyRepository extends JpaRepository<ActivityApply, Long>, ActivityApplyRepositoryCustom {
}
