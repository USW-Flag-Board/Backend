package com.FlagHome.backend.module.token;

import com.FlagHome.backend.common.RepositoryTest;
import com.FlagHome.backend.module.token.domain.RecoveryToken;
import com.FlagHome.backend.module.token.domain.Token;
import com.FlagHome.backend.module.token.domain.repository.TokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenRepositoryTest extends RepositoryTest {
    @Autowired
    private TokenRepository tokenRepository;

    @Test
    @DisplayName("아이디/비밀번호 전용 토큰 찾기 테스트")
    void findTokenTest() {
        // given
        String email = "gmlwh124@suwon.ac.kr";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFive = now.plusMinutes(5);

        Token token1 = RecoveryToken.builder()
                .key(email)
                .expiredAt(now)
                .build();

        Token token2 = RecoveryToken.builder()
                .key(email)
                .expiredAt(afterFive)
                .build();

        tokenRepository.saveAllAndFlush(Arrays.asList(token1, token2));

        // when
        Optional<Token> foundToken = tokenRepository.findFirstByKeyOrderByIdDesc(email);

        // then
        assertThat(foundToken.get()).isNotNull();
        assertThat(foundToken.get().getKey()).isEqualTo(email);
        assertThat(foundToken.get().getExpiredAt()).isEqualTo(afterFive);
    }
}
