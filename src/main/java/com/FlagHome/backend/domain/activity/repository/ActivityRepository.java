package com.FlagHome.backend.domain.activity.repository;

import com.FlagHome.backend.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
