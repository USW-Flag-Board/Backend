package com.FlagHome.backend.domain.auth;

import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.auth.service.AuthService;
import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.global.utility.InputValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceSliceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private InputValidator inputValidator;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private MailService mailService;

    @Test
    @DisplayName("회원가입 정보입력 단계 테스트")
    void joinTest() {
        // given

        // when

        // then
    }
}
