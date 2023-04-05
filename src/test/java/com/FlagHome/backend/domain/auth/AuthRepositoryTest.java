package com.FlagHome.backend.domain.auth;

import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(QueryDslConfig.class)
public class AuthRepositoryTest {
    @Autowired
    private AuthRepository authRepository;

    @Test
    @DisplayName("인증 정보 가져오기 테스트")
    void findFirstByEmailOrderByCreatedAtTest() {
        String email = "gmlwh124@suwon.ac.kr";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFive = now.plusMinutes(5);

        // given
        AuthInformation authInformation1 = AuthInformation.builder()
                .email(email)
                .createdAt(now)
                .build();

        AuthInformation authInformation2 = AuthInformation.builder()
                .email(email)
                .createdAt(afterFive)
                .build();

        authRepository.saveAll(Arrays.asList(authInformation1, authInformation2));

        // when
        AuthInformation foundAuthInformation = authRepository.findFirstByEmailOrderByCreatedAtDesc(email).get();

        // then
        assertThat(foundAuthInformation.getCreatedAt()).isEqualTo(afterFive);
        assertThat(foundAuthInformation.getEmail()).isEqualTo(email);
    }
}