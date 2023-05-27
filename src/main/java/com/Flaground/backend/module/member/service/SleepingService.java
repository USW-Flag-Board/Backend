package com.Flaground.backend.module.member.service;

import com.Flaground.backend.module.member.domain.repository.SleepingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SleepingService {
    private final SleepingRepository sleepingRepository;

    public boolean existsByLoginId(String loginId) {
        return sleepingRepository.existsByLoginId(loginId);
    }

    public boolean existsByEmail(String email) {
        return sleepingRepository.existsByEmail(email);
    }

    @Transactional
    public void reactiveIfSleeping(String loginId) {
        sleepingRepository.findByLoginId(loginId)
                .ifPresent(sleeping -> {
                    sleeping.reactivate();
                    sleepingRepository.deleteByLoginId(loginId);
                });
    }
}
