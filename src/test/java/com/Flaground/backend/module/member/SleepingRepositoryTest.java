package com.Flaground.backend.module.member;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.Sleeping;
import com.Flaground.backend.module.member.domain.enums.MemberStatus;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.member.domain.repository.SleepingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class SleepingRepositoryTest extends RepositoryTest {
    @Autowired
    private SleepingRepository sleepingRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Sleeping sleeping;
    private Member member;

    @BeforeEach
    void setup() {
        final String loginId = "gmlwh124";
        final String email = "gmlwh124@suwon.ac.kr";

        Avatar avatar = Avatar.builder().build();
        member = memberRepository.save(Member.builder().loginId(loginId).email(email).avatar(avatar).build());
        sleeping = sleepingRepository.save(Sleeping.of(member));
    }

    @Test
    void 휴면유저_삭제_쿼리_테스트() {
        // given
        final String loginId = "gmlwh124";

        // when
        sleepingRepository.deleteByLoginId(loginId);

        // then
        Sleeping sleeping = sleepingRepository.findByLoginId(loginId).orElse(null);
        assertThat(sleeping).isNull();
    }

    @Test
    void 휴면유저_활성화_테스트() {
        // given
        final String loginId = "gmlwh124";
        final String email = "gmlwh124@suwon.ac.kr";
        member.deactivate();

        // when
        sleepingRepository.findByLoginId(loginId)
                .ifPresent(Sleeping::reactivate);

        // then
        assertThat(member.getLoginId()).isEqualTo(loginId);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getStatus()).isEqualTo(MemberStatus.NORMAL);
    }
}
