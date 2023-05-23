package com.Flaground.backend.module.activity.domain.repository;

import com.Flaground.backend.module.activity.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long>, ActivityRepositoryCustom {
    Optional<Activity> findById(Long activityId);
    boolean existsById(Long activityId);
}