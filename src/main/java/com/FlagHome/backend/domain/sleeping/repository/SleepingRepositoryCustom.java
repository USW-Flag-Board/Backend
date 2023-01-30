package com.FlagHome.backend.domain.sleeping.repository;

import com.FlagHome.backend.domain.sleeping.entity.Sleeping;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SleepingRepositoryCustom {
    List<Sleeping> getAllSleeping();
}
