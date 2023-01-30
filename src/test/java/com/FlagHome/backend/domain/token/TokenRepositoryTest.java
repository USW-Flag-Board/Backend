package com.FlagHome.backend.domain.token;

import com.FlagHome.backend.domain.token.entity.FindRequestToken;
import com.FlagHome.backend.domain.token.entity.Token;
import com.FlagHome.backend.domain.token.repository.TokenRepository;
import com.FlagHome.backend.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(QueryDslConfig.class)
public class TokenRepositoryTest {
    @Autowired
    private TokenRepository tokenRepository;

    @Test
    @DisplayName("아이디/비밀번호 전용 토큰 찾기 테스트")
    void findTokenTest() {
        // given
        String email = "gmlwh124@suwon.ac.kr";
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterFive = now.plusMinutes(5);

        Token token1 = FindRequestToken.builder()
                .key(email)
                .expiredAt(now)
                .build();

        Token token2 = FindRequestToken.builder()
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
