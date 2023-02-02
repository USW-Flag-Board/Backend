package com.FlagHome.backend.domain.sleeping.service;

import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.sleeping.repository.SleepingRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SleepingService {

    private final SleepingRepository sleepingRepository;

    private final MemberRepository memberRepository;

    private final MemberService memberService;

    @Transactional
    public void changeSleepToMember(String loginId) {
        Sleeping sleeping = findByLoginId(loginId);
        Member member = memberService.findByLoginId(loginId);
        member.sleepToMember(sleeping);
        sleepingRepository.delete(sleeping);
    }

    @Transactional
    //@Scheduled(cron = "000000")
    public void deleteExpiredSleep() { //만료된사람들지우는거 네임 다시
        List<Sleeping> sleepingList = sleepingRepository.getAllSleeping();
        sleepingRepository.deleteAllInBatch(sleepingList);
    }

    public Sleeping findByLoginId(String loginId) {
        return sleepingRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.SLEEP_NOT_FOUND));
    }
}
