package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.member.controller.dto.FindResponse;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.token.entity.FindRequestToken;
import com.FlagHome.backend.domain.token.entity.Token;
import com.FlagHome.backend.domain.token.service.FindRequestTokenService;
import com.FlagHome.backend.infra.aws.ses.service.MailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemberServiceSliceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MailService mailService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FindRequestTokenService findRequestTokenService;

    @Test
    @DisplayName("아이디 찾기 테스트")
    void findIdTest() {
        // given
        String name = "문희조";
        String email = "gmlwh124@suwon.ac.kr";
        String certification = "123456";
        LocalDateTime expireAt = LocalDateTime.now();
        
        Member member = Member.builder()
                .name(name)
                .email(email)
                .build();
        
        Token findRequestToken = FindRequestToken.builder()
                .key(email)
                .value(certification)
                .expiredAt(expireAt)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(findRequestTokenService.issueToken(anyString(), anyString())).willReturn(findRequestToken);
        doNothing().when(mailService).sendFindCertification(anyString(), anyString());

        // when
        FindResponse findResponse = memberService.findId(name, email);

        // then
        then(findRequestTokenService).should(times(1)).issueToken(anyString(), anyString());
        then(mailService).should(times(1)).sendFindCertification(anyString(), anyString());
        assertThat(findResponse.getEmail()).isEqualTo(email);
        assertThat(findResponse.getDeadLine()).isEqualTo(expireAt);
    }

    @Test
    @DisplayName("비밀번호 찾기 테스트")
    void findPasswordTest() {
        // given
        String loginId = "gmlwh124";
        String email = "gmlwh124@suwon.ac.kr";
        String certification = "123456";
        LocalDateTime expireAt = LocalDateTime.now();

        Member member = Member.builder()
                .loginId(loginId)
                .email(email)
                .build();

        Token findRequestToken = FindRequestToken.builder()
                .key(email)
                .value(certification)
                .expiredAt(expireAt)
                .build();

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(findRequestTokenService.issueToken(anyString(), anyString())).willReturn(findRequestToken);
        doNothing().when(mailService).sendFindCertification(anyString(), anyString());

        // when
        FindResponse findResponse = memberService.findPassword(loginId, email);

        // then
        then(memberRepository).should(times(1)).findByEmail(anyString());
        then(findRequestTokenService).should(times(1)).issueToken(anyString(), anyString());
        then(mailService).should(times(1)).sendFindCertification(anyString(), anyString());
        assertThat(findResponse.getEmail()).isEqualTo(email);
        assertThat(findResponse.getDeadLine()).isEqualTo(expireAt);
    }

    @Test
    @DisplayName("인증번호 인증 테스트")
    void validateCertificationTest() {
        // given
        String email = "gmlwh124@suwon.ac.kr";
        String certification = "123456";
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(5);

        Member member = Member.builder().build();

        Token findRequestToken = FindRequestToken.builder()
                .key(email)
                .value(certification)
                .expiredAt(expireAt)
                .build();

        given(findRequestTokenService.findToken(anyString())).willReturn(findRequestToken);

        // when
        memberService.validateCertification(email, certification);

        // then
        Token findToken = findRequestTokenService.findToken(email);
        then(findRequestTokenService).should(times(2)).findToken(anyString());
        assertThat(findRequestToken.getKey()).isEqualTo(findToken.getKey());
        assertThat(findRequestToken.getExpiredAt()).isEqualTo(findToken.getExpiredAt());
    }
}
