package com.Flaground.backend.module.member.blackList;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.module.member.domain.BlackList;
import com.Flaground.backend.module.member.domain.repository.BlackListRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class BlackListRepositoryTest extends RepositoryTest {
    @Autowired
    private BlackListRepository blackListRepository;

    @Test
    void 블랙리스트_조회_테스트() {
        // given
        final String email = "gmlwh124@suwon.ac.kr";
        final String wrongEmail = "hejow124@suwon.ac.kr";
        blackListRepository.save(BlackList.suspend(email));
        blackListRepository.save(BlackList.ban(wrongEmail));

        // when
        boolean exists = blackListRepository.existsByEmail(email);
        boolean notExists = blackListRepository.existsByEmail(wrongEmail);

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
