package com.Flaground.backend.module.member.service;

import com.Flaground.backend.module.member.domain.Sleeping;
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
    public void reactivateMember(String loginId) { // todo: 불필요한 삭제쿼리 발생
        reactiveIfPresent(loginId);
        sleepingRepository.deleteByLoginId(loginId);
    }

    private void reactiveIfPresent(String loginId) { // todo: reactive 되는지 테스트 작성하기
        sleepingRepository.findByLoginId(loginId)
                .ifPresent(Sleeping::reactivate);
    }
}
