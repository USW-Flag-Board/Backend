package com.FlagHome.backend.domain.memberActivity.repository;

import com.FlagHome.backend.domain.memberActivity.entity.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long> {
}
