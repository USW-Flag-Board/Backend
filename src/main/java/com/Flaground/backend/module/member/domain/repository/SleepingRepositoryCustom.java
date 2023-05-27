package com.Flaground.backend.module.member.domain.repository;

import com.Flaground.backend.module.member.domain.Sleeping;

import java.util.List;

public interface SleepingRepositoryCustom {
    List<Sleeping> getAllSleeping();
    void deleteByLoginId(String loginId);
}
