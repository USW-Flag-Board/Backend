package com.FlagHome.backend.module.member.domain.repository;

import com.FlagHome.backend.module.member.domain.Sleeping;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SleepingRepositoryCustom {
    List<Sleeping> getAllSleeping();
}
