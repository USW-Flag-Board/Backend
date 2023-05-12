package com.Flaground.backend.module.activity.memberactivity.repository;

import com.Flaground.backend.module.activity.memberactivity.entity.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long>, MemberActivityRepositoryCustom {
}
