package com.FlagHome.backend.domain.member.sleeping.service;

import com.FlagHome.backend.domain.member.sleeping.repository.SleepingRepository;
import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SleepingService {

    private final SleepingRepository sleepingRepository;

    @Transactional
    public void changeSleepToMember(Member member, String loginId) {
        Sleeping sleeping = findByLoginId(loginId);
        member.sleepToMember(sleeping);
        sleepingRepository.delete(sleeping);
    }

    @Transactional
    //@Scheduled(cron = "000000")
    public void deleteExpiredSleep() {
        List<Sleeping> sleepingList = sleepingRepository.getAllSleeping();
        sleepingRepository.deleteAllInBatch(sleepingList);
    }

    public Sleeping findByLoginId(String loginId) {
        return sleepingRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.SLEEP_NOT_FOUND));
    }
}
