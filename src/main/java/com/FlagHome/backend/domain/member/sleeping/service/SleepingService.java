package com.FlagHome.backend.domain.member.sleeping.service;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.member.sleeping.repository.SleepingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
    public void saveAllSleeping(List<Sleeping> sleepingList) {
        sleepingRepository.saveAll(sleepingList);
    }

    @Transactional
    public void reactivateMember(Member member, Sleeping sleeping) {
        member.reactivate(sleeping);
        sleepingRepository.delete(sleeping);
    }

    @Transactional
    //@Scheduled(cron = "000000")
    public void deleteExpiredSleep() {
        List<Sleeping> sleepingList = sleepingRepository.getAllSleeping();
        sleepingRepository.deleteAllInBatch(sleepingList);
    }

    public Sleeping findByLoginId(String loginId) {
        return sleepingRepository.findByLoginId(loginId).orElse(null);
    }
}
