package com.Flaground.backend.module.member.domain.BlackState;

import com.Flaground.backend.module.member.domain.BlackList;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.BlackListRepository;

public class Ban implements BlackState {
    private static final String SUSPEND_MESSAGE = "계정을 추방시켰습니다.";
    private static final int SUSPEND_POINT = 20;

    @Override
    public String dealPenalty(Member member, int penalty, BlackListRepository blackListRepository) {
        int totalPenalty = member.applyPenalty(penalty);
        return totalPenalty >= SUSPEND_POINT ? suspend(member, blackListRepository) : DEFAULT_MESSAGE;
    }

    private String suspend(Member member, BlackListRepository blackListRepository) {
        blackListRepository.save(BlackList.suspend(member.getEmail()));
        member.withdraw();
        return SUSPEND_MESSAGE;
    }
}
