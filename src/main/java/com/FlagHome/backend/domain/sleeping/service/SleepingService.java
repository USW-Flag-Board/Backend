package com.FlagHome.backend.domain.sleeping.service;

import com.FlagHome.backend.domain.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.sleeping.repository.SleepingRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SleepingService {

    private final SleepingRepository sleepingRepository;

    private final PasswordEncoder passwordEncoder;

    private final MemberService memberService;

    public void isSleepMember(String email) {
        if (sleepingRepository.existsByEmail(passwordEncoder.encode(email))) {
            throw new CustomException(ErrorCode.EMAIL_NOT_FOUND);
        }
    }

    @Transactional
    public void changeSleepToMember(String email) {
        Sleeping sleep = sleepingRepository.findByEmail(passwordEncoder.encode(email))
                .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));
        Member member = memberService.findById(sleep.getMember().getId());

    }

    @Transactional
    //@Scheduled(cron = "000000")
    public void deleteSleepMember() {
        List<Sleeping> sleepingList = sleepingRepository.getAllSleeping();
        sleepingRepository.deleteAllInBatch(sleepingList);
    }
}
