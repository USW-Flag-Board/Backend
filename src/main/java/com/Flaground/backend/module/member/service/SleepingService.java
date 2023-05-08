package com.Flaground.backend.module.member.service;

import com.Flaground.backend.module.member.domain.Sleeping;
import com.Flaground.backend.module.member.domain.repository.SleepingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SleepingService {
    private final SleepingRepository sleepingRepository;

    public boolean existsByLoginId(String loginId) {
        return sleepingRepository.existsByLoginId(loginId);
    }

    public boolean existsByEmail(String email) {
        return sleepingRepository.existsByEmail(email);
    }

    public void saveAll(List<Sleeping> sleepingList) {
        sleepingRepository.saveAll(sleepingList);
    }

    public void reactivateMember(String loginId) {
        reactiveIfPresent(loginId);
        sleepingRepository.deleteByLoginId(loginId);
    }

    //@Scheduled(cron = "000000")
    public void deleteExpiredSleep() {
        List<Sleeping> sleepingList = sleepingRepository.getAllSleeping();
        sleepingRepository.deleteAllInBatch(sleepingList);
    }

    private void reactiveIfPresent(String loginId) {
        sleepingRepository.findByLoginId(loginId)
                .ifPresent(Sleeping::reactivate);
    }
}
