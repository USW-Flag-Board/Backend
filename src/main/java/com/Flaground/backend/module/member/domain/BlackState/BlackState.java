package com.Flaground.backend.module.member.domain.BlackState;

import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.BlackListRepository;

public interface BlackState {
    String DEFAULT_MESSAGE = "페널티가 부과되었습니다.";
    String dealPenalty(Member member, int penalty, BlackListRepository blackListRepository);
}
