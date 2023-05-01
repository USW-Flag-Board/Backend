package com.FlagHome.backend.module.auth;

import com.FlagHome.backend.module.auth.controller.dto.request.JoinRequest;
import com.FlagHome.backend.module.auth.controller.dto.response.SignUpResponse;
import com.FlagHome.backend.module.auth.domain.AuthInformation;
import com.FlagHome.backend.module.auth.domain.JoinType;
import com.FlagHome.backend.module.auth.mapper.AuthMapper;
import com.FlagHome.backend.module.auth.domain.repository.AuthRepository;
import com.FlagHome.backend.module.auth.service.AuthService;
import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.member.domain.enums.Role;
import com.FlagHome.backend.module.member.domain.repository.MemberRepository;
import com.FlagHome.backend.module.token.dto.TokenResponse;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.utility.RandomGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtilizer jwtUtilizer;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 아이디_유효성_검사_테스트() {
        // given
        String loginId = "gmlwh124";
        String noneLoginId = "hejow124";

        memberRepository.save(Member.builder().loginId(loginId).build());

        // when
        boolean shouldBeTrue = authService.validateDuplicateLoginId(loginId);
        boolean shouldBeFalse = authService.validateDuplicateLoginId(noneLoginId);

        // then
        assertThat(shouldBeTrue).isTrue();
        assertThat(shouldBeFalse).isFalse();
    }


    @Test
    void 이메일_유효성_검사_테스트() {
        // given
        String email = "gmlwh124@suwon.ac.kr";
        String noneEmail = "hejow124@suwon.ac.kr";

        memberRepository.save(Member.builder().email(email).build());

        // when
        boolean shouldBeTrue = authService.validateDuplicateEmail(email);
        boolean shouldBeFalse = authService.validateDuplicateEmail(noneEmail);

        // then
        assertThat(shouldBeTrue).isTrue();
        assertThat(shouldBeFalse).isFalse();
    }

    @Nested
    class 회원가입_테스트 {
        private final String loginId = "gmlwh124";
        private final String password = "qwer1234!";
        private final String email = "gmlwh124@suwon.ac.kr";

        @Test
        void 일반유저_회원가입_테스트() {
            // given
            final JoinType joinType = JoinType.NORMAL;
            final String certification = RandomGenerator.getRandomNumber();

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .joinType(joinType)
                    .build();

            authRepository.saveAndFlush(AuthInformation.of(authMapper.mapFrom(joinRequest), certification));

            // when
            SignUpResponse signUpResponse = authService.signUp(email, certification);

            // then
            Member member = memberRepository.findByLoginId(loginId).orElse(null);
            assertThat(signUpResponse.getEmail()).isEqualTo(email);
            assertThat(member).isNotNull();
            assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
            assertThat(member.getRole()).isEqualTo(Role.from(joinType));
        }

        @Test
        void 동아리원_회원가입_테스트() {
            // given
            final JoinType joinType = JoinType.CREW;
            final String certification = RandomGenerator.getRandomNumber();

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .joinType(joinType)
                    .build();

            authRepository.saveAndFlush(AuthInformation.of(authMapper.mapFrom(joinRequest), certification));

            // when
            SignUpResponse signUpResponse = authService.signUp(email, certification);

            // then
            AuthInformation signupAuthInformation = authRepository.findFirstByEmailOrderByCreatedAtDesc(email).orElse(null);
            assertThat(signUpResponse.getEmail()).isEqualTo(email);
            assertThat(signupAuthInformation).isNotNull();
            assertThat(signupAuthInformation.isAuthorizedCrew()).isTrue();
        }

        @Test
        void 인증번호_오류_테스트() {
            final String certification = "123456";
            final String wrongCertification = "234567";

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .build();

            authRepository.saveAndFlush(AuthInformation.of(authMapper.mapFrom(joinRequest), certification));

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.signUp(email, wrongCertification))
                    .withMessage(ErrorCode.CERTIFICATION_NOT_MATCH.getMessage());
        }
    }

    @Nested
    class 로그인_테스트 {
        private final String loginId = "gmlwh124";
        private final String password = "qwer1234!";
        private final Role role = Role.ROLE_USER;

        @Test
        void 로그인_성공_테스트() {
            // given
            Member savedMember = memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .build());

            // when
            TokenResponse tokenResponse = authService.login(loginId, password);
            entityManager.clear(); // update query

            // then
            assertThat(tokenResponse.getAccessToken()).isNotNull();
            assertThat(tokenResponse.getRefreshToken()).isNotNull();
            assertThat(tokenResponse.getAccessTokenExpiresIn()).isNotNull();
            assertThat(tokenResponse.getGrantType()).isEqualTo("Bearer");

            String accessToken = tokenResponse.getAccessToken();
            assertThat(jwtUtilizer.validateToken(accessToken)).isTrue();

            Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
            long memberId = Long.parseLong(authentication.getName());
            Member member = memberRepository.findByLoginId(loginId).orElse(null);
            assertThat(member).isNotNull();
            assertThat(savedMember.getUpdatedAt()).isNotEqualTo(member.getUpdatedAt());
            assertThat(member.getId()).isEqualTo(memberId);
        }

        @Test
        void 로그인_실패_테스트() {
            // given
            final String wrongPassword = "wert2345@";

            Member member = Member.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .build();

            memberRepository.save(member);

            // when, then
            assertThatExceptionOfType(BadCredentialsException.class)
                    .isThrownBy(() -> authService.login(loginId, wrongPassword));
        }
    }


    @Test
    void 토큰_재발급_테스트() {
        // given
        final String loginId = "gmlwh124";
        final String password = "qwer1234!";
        final Role role = Role.ROLE_USER;

        memberRepository.save(Member.builder()
                        .loginId(loginId)
                        .password(passwordEncoder.encode(password))
                        .role(role)
                        .build());

        TokenResponse tokenResponse = authService.login(loginId, password);

        // when
        TokenResponse reissueToken = authService.reissueToken(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        // then
        assertThat(reissueToken.getAccessToken()).isNotNull();
        assertThat(reissueToken.getRefreshToken()).isNotNull();
        assertThat(reissueToken.getGrantType()).isNotNull();
        assertThat(reissueToken.getAccessTokenExpiresIn()).isNotNull();

        String accessToken = reissueToken.getAccessToken();
        assertThat(jwtUtilizer.validateToken(accessToken)).isTrue();

        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
        long memberId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByLoginId(loginId).orElse(null);
        assertThat(member).isNotNull();
        assertThat(member.getId()).isEqualTo(memberId);
    }
}
