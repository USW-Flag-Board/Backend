package com.Flaground.backend.module.member.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.member.domain.BlackState.BlackState;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlackListService {
    private final BlackListRepository blackListRepository;

    public void validateBlackList(String email) {
        if (isBlackList(email)) {
            throw new CustomException(ErrorCode.BLACKED_EMAIL);
        }
    }

    @Transactional
    public String dealReport(Member member, int penalty) {
        BlackState blackState = member.getState();
        return blackState.dealPenalty(member, penalty, blackListRepository);
    }

    // todo: 정지 풀어줄 때 블랙리스트 조심하기

    private boolean isBlackList(String email) {
        return blackListRepository.existsByEmail(email);
    }
}
