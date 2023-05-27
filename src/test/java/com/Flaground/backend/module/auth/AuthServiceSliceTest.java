package com.Flaground.backend.module.auth;

import com.Flaground.backend.common.MockServiceTest;
import com.Flaground.backend.global.utility.RandomGenerator;
import com.Flaground.backend.module.auth.controller.dto.response.JoinResponse;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.domain.repository.AuthRepository;
import com.Flaground.backend.module.auth.service.AuthService;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.infra.aws.ses.service.AwsSESServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

public class AuthServiceSliceTest extends MockServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private AwsSESServiceImpl mailService;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private MemberService memberService;

    @Test
    void 회원가입_정보_입력_테스트() {
        // given
        final String email = "gmlwh124@suwon.ac.kr";
        final String cetification = RandomGenerator.getRandomNumber();
        AuthInformation authInformation = AuthInformation.builder()
                .email(email)
                .certification(cetification)
                .build();

        given(authRepository.save(any())).willReturn(authInformation);
        willDoNothing().given(mailService).sendCertification(anyString(), anyString());

        // when
        JoinResponse response = JoinResponse.from(authService.join(authInformation));

        // then
        then(authRepository).should(times(1)).save(any());
        then(mailService).should(times(1)).sendCertification(anyString(), anyString());
        assertThat(response.getEmail()).isEqualTo(email);
    }
}
