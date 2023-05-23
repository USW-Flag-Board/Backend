package com.Flaground.backend.module.activity.domain.repository;

import com.Flaground.backend.module.activity.domain.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long>, MemberActivityRepositoryCustom {
}
