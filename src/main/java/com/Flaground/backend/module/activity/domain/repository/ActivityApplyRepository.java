package com.Flaground.backend.module.activity.domain.repository;

import com.Flaground.backend.module.activity.domain.ActivityApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityApplyRepository extends JpaRepository<ActivityApply, Long>, ActivityApplyRepositoryCustom {
}
