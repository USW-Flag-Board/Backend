package com.Flaground.backend.module.member.domain.repository;

import com.Flaground.backend.module.member.domain.Sleeping;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SleepingRepositoryCustom {
    List<Sleeping> getAllSleeping();
    void deleteByLoginId(String loginId);
}