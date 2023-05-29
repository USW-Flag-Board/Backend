package com.Flaground.backend.module.member.blackList;

import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.module.member.domain.BlackList;
import com.Flaground.backend.module.member.domain.repository.BlackListRepository;
import com.Flaground.backend.module.member.service.BlackListService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BlackListServiceTest {
    @Autowired
    private BlackListRepository blackListRepository;

    @Autowired
    private BlackListService blackListService;

    @AfterEach
    void clean() {
        blackListRepository.deleteAllInBatch();
    }

    @Test
    void 블랙리스트_유효성_검사_테스트() {
        // given
        final String email = "gmlwh124@suwon.ac.kr";
        blackListRepository.save(BlackList.suspend(email));

        // when, then
        assertThatThrownBy(() -> blackListService.validateBlackList(email))
                .withFailMessage(ErrorCode.BLACKED_EMAIL.getMessage());
    }
}
