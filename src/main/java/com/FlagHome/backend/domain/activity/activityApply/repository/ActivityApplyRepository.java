package com.FlagHome.backend.domain.activity.activityApply.repository;

import com.FlagHome.backend.domain.activity.activityApply.entity.ActivityApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityApplyRepository extends JpaRepository<ActivityApply, Long>, ActivityApplyRepositoryCustom {
}
