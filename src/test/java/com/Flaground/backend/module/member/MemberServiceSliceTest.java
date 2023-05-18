package com.Flaground.backend.module.member;

import com.Flaground.backend.common.MockServiceTest;
import com.Flaground.backend.global.utility.RandomGenerator;
import com.Flaground.backend.infra.aws.ses.service.MailService;
import com.Flaground.backend.module.member.controller.dto.response.RecoveryResponse;
import com.Flaground.backend.module.member.controller.dto.response.RecoveryResultResponse;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.module.token.domain.RecoveryToken;
import com.Flaground.backend.module.token.domain.Token;
import com.Flaground.backend.module.token.service.RecoveryTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

public class MemberServiceSliceTest extends MockServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MailService mailService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RecoveryTokenService recoveryTokenService;

    @Test
    void 아이디_찾기_테스트() {
        // given
        final String name = "문희조";
        final String email = "gmlwh124@suwon.ac.kr";
        final String certification = RandomGenerator.getRandomNumber();

        Member member = Member.builder()
                .name(name)
                .email(email)
                .build();

        Token findRequestToken = RecoveryToken.of(email, certification);

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(recoveryTokenService.issueToken(anyString(), anyString())).willReturn(findRequestToken);
        doNothing().when(mailService).sendFindCertification(anyString(), anyString());

        // when
        RecoveryResponse response = memberService.findId(name, email);

        // then
        then(recoveryTokenService).should(times(1)).issueToken(anyString(), anyString());
        then(mailService).should(times(1)).sendFindCertification(anyString(), anyString());
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getDeadLine()).isEqualTo(findRequestToken.getExpiredAt());
    }

    @Test
    void 비밀번호_찾기_테스트() {
        // given
        final String loginId = "gmlwh124";
        final String email = "gmlwh124@suwon.ac.kr";
        final String certification = RandomGenerator.getRandomNumber();

        Member member = Member.builder()
                .loginId(loginId)
                .email(email)
                .build();

        Token findRequestToken = RecoveryToken.of(email, certification);

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(recoveryTokenService.issueToken(anyString(), anyString())).willReturn(findRequestToken);
        doNothing().when(mailService).sendFindCertification(anyString(), anyString());

        // when
        RecoveryResponse response = memberService.findPassword(loginId, email);

        // then
        then(memberRepository).should(times(1)).findByEmail(anyString());
        then(recoveryTokenService).should(times(1)).issueToken(anyString(), anyString());
        then(mailService).should(times(1)).sendFindCertification(anyString(), anyString());
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getDeadLine()).isEqualTo(findRequestToken.getExpiredAt());
    }

    @Test
    void 아이디_비밀번호_찾기_인증_테스트() {
        // given
        final String loginId = "gmlwh124";
        final String email = "gmlwh124@suwon.ac.kr";
        final String certification = RandomGenerator.getRandomNumber();

        Member member = Member.builder().loginId(loginId).email(email).build();
        Token findRequestToken = RecoveryToken.of(email, certification);

        given(recoveryTokenService.findToken(anyString())).willReturn(findRequestToken);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when
        RecoveryResultResponse response = memberService.validateCertification(email, certification);

        // then
        then(recoveryTokenService).should(times(1)).findToken(anyString());
        then(memberRepository).should(times(1)).findByEmail(anyString());
        assertThat(response.getLoginId()).isEqualTo(loginId);
        assertThat(response.getEmail()).isEqualTo(email);
    }
}
