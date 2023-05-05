package com.FlagHome.backend.module.member;

import com.FlagHome.backend.common.RepositoryTest;
import com.FlagHome.backend.module.member.domain.Sleeping;
import com.FlagHome.backend.module.member.domain.repository.SleepingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class SleepingRepositoryTest extends RepositoryTest {
    @Autowired
    private SleepingRepository sleepingRepository;

    @Test
    void 휴면유저_삭제_쿼리_테스트() {
        // given
        final String loginId = "gmlwh124";
        sleepingRepository.save(Sleeping.builder().loginId(loginId).build());

        // when
        sleepingRepository.deleteByLoginId(loginId);

        // then
        Sleeping sleeping = sleepingRepository.findByLoginId(loginId).orElse(null);
        assertThat(sleeping).isNull();
    }
}
