package com.Flaground.backend.module.member.domain.BlackState;

import com.Flaground.backend.module.member.domain.BlackList;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.BlackListRepository;

public class Watching implements BlackState {
    private static final String BAN_MESSAGE = "계정이 3일 정지되었습니다.";
    private static final int BAN_POINT = 10;

    @Override
    public String dealPenalty(Member member, int penalty, BlackListRepository blackListRepository) {
        int totalPenalty = member.applyPenalty(penalty);
        return totalPenalty >= BAN_POINT ? ban(member, blackListRepository) : DEFAULT_MESSAGE;
    }

    private String ban(Member member, BlackListRepository blackListRepository) {
        blackListRepository.save(BlackList.ban(member.getEmail()));
        member.ban();
        return BAN_MESSAGE;
    }
}
