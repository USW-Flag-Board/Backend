package com.FlagHome.backend.domain.auth;

import com.FlagHome.backend.domain.auth.dto.JoinRequest;
import com.FlagHome.backend.domain.auth.dto.LoginRequest;
import com.FlagHome.backend.domain.auth.dto.SignUpRequest;
import com.FlagHome.backend.domain.auth.dto.SignUpResponse;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.auth.service.AuthService;
import com.FlagHome.backend.domain.member.Role;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.token.dto.TokenRequest;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.utility.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private JwtUtilizer jwtUtilizer;

    @Nested
    @DisplayName("아이디 유효성 테스트")
    class validateIdTest {
        @BeforeEach
        void beforeValidateId() {
            String loginId = "gmlwh124";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(password)
                    .build());
        }

        @Test
        @DisplayName("아이디가 중복이 아님")
        void validateIdSuccessTest() {
            String validId = "hejow124";

            assertThatNoException()
                    .isThrownBy(() -> authService.validateDuplicateLoginId(validId));
        }

        @Test
        @DisplayName("아이디가 중복되어 실패")
        void validateIdFailTest() {
            String overlapedId = "gmlwh124";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.validateDuplicateLoginId(overlapedId))
                    .withMessage(ErrorCode.LOGIN_ID_EXISTS.getMessage());
        }
    }

    @Nested
    @DisplayName("이메일 유효성 테스트")
    class validateEmailTest {
        @Test
        @DisplayName("수원대 이메일이며 중복이 아님")
        void validateEmailSuccessTest() {
            String loginId = "gmlwh124";
            String email = "gmlwh124@suwon.ac.kr";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(password)
                    .build());

            assertThatNoException()
                    .isThrownBy(() -> authService.validateEmail("hejow124@suwon.ac.kr"));
        }

        @Test
        @DisplayName("수원대 이메일이 아니라 실패")
        void validateUSWEmailFailTest() {
            String email = "gmlwh124@naver.com";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.validateEmail(email))
                    .withMessage(ErrorCode.NOT_USW_EMAIL.getMessage());
        }

        @Test
        @DisplayName("수원대 이메일이지만 중복이라서 실패")
        void validateEmailFailTest() {
            String loginId = "gmlwh124";
            String email = "gmlwh124@suwon.ac.kr";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(password)
                    .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.validateEmail(email))
                    .withMessage(ErrorCode.EMAIL_EXISTS.getMessage());
        }
    }

    @Nested
    @DisplayName("회원가입(join) 테스트")
    class signUpJoinTest {
        @Test
        @DisplayName("회원가입 join 성공")
        void joinSuccessTest() {
            // given
            String loginId = "gmlwh124";
            String password = "qwer1234!";
            String email = "gmlwh124@suwon.ac.kr";

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .build();

            // when
            authService.join(joinRequest);

            // then
            AuthInformation authInformation = authRepository.findByEmail(email).get();
            assertThat(authInformation).isNotNull();
            assertThat(authInformation.getLoginId()).isEqualTo(loginId);
            assertThat(authInformation.getPassword()).isEqualTo(password);
        }

        @Test
        @DisplayName("비밀번호 유효성 검사 실패로 join 실패")
        void joinFailTest() {
            String loginId = "gmlwh124";
            String password = "1234";
            String email = "gmlwh124@suwon.ac.kr";

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .build();

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.join(joinRequest))
                    .withMessage(ErrorCode.INVALID_PASSWORD.getMessage());
        }
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class signUpTest {
        @Test
        @DisplayName("일반유저 회원가입 성공")
        void signUpSuccessTest() {
            // given
            String loginId = "gmlwh124";
            String password = "qwer1234!";
            String email = "gmlwh124@suwon.ac.kr";
            JoinType joinType = JoinType.NORMAL;
            String certification = RandomGenerator.getRandomNumber();

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .joinType(joinType)
                    .build();

            AuthInformation authInformation = AuthInformation.of(joinRequest, certification);
            authRepository.saveAndFlush(authInformation);

            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .email(email)
                    .certification(certification)
                    .build();

            // when
            SignUpResponse signUpResponse = authService.signUp(signUpRequest);

            // then
            assertThat(signUpResponse.getEmail()).isEqualTo(email);

            Member member = memberRepository.findByLoginId(loginId).get();
            assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
            assertThat(member.getRole()).isEqualTo(Role.from(joinType));
        }

        @Test
        @DisplayName("동아리회원 회원가입 성공")
        void crewSignUpSuccessTest() {
            // given
            String loginId = "gmlwh124";
            String password = "qwer1234!";
            String email = "gmlwh124@suwon.ac.kr";
            String certification = RandomGenerator.getRandomNumber();

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .joinType(JoinType.CREW)
                    .build();

            AuthInformation authInformation = AuthInformation.of(joinRequest, certification);
            authRepository.saveAndFlush(authInformation);

            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .email(email)
                    .certification(certification)
                    .build();

            // when
            SignUpResponse signUpResponse = authService.signUp(signUpRequest);

            // then
            assertThat(signUpResponse.getEmail()).isEqualTo(email);

            AuthInformation signupAuthInformation = authRepository.findByEmail(signUpResponse.getEmail()).get();
            assertThat(signupAuthInformation.isAuthorizedCrew()).isTrue();
        }

        @Test
        @DisplayName("회원가입 인증번호 오류로 실패")
        void signupCertificationFailTest() {
            String loginId = "gmlwh124";
            String password = "qwer1234!";
            String email = "gmlwh124@suwon.ac.kr";
            String certification = "123456";
            String wrongCertification = "234567";

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .build();

            authRepository.saveAndFlush(AuthInformation.of(joinRequest, certification));

            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .email(email)
                    .certification(wrongCertification)
                    .build();

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.signUp(signUpRequest))
                    .withMessage(ErrorCode.CERTIFICATION_NOT_MATCH.getMessage());
        }
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
        // given
        String loginId = "gmlwh124";
        String password = "1234";

        memberRepository.saveAndFlush(Member.builder()
                        .loginId(loginId)
                        .password(passwordEncoder.encode(password))
                        .role(Role.ROLE_USER)
                        .build());

        LoginRequest logInRequest = LoginRequest.builder()
                .loginId(loginId)
                .password(password)
                .build();

        // when
        TokenResponse tokenResponse = authService.login(logInRequest);

        // then : 정상적으로 발급되는 지, 유효한 지, 데이터가 일치하는 지
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getRefreshToken()).isNotNull();
        assertThat(tokenResponse.getAccessTokenExpiresIn()).isNotNull();
        assertThat(tokenResponse.getGrantType()).isEqualTo("Bearer");

        String accessToken = tokenResponse.getAccessToken();

        assertThat(jwtUtilizer.validateToken(accessToken)).isTrue();

        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
        long memberId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByLoginId(loginId).get();
        assertThat(member.getId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("토큰 재발급 테스트")
    void reIssueToken() {
        // given
        String loginId = "gmlwh124";
        String password = "1234";

        memberRepository.saveAndFlush(Member.builder()
                        .loginId(loginId)
                        .password(passwordEncoder.encode(password))
                        .role(Role.ROLE_USER)
                        .build());

        LoginRequest logInRequest = LoginRequest.builder()
                .loginId(loginId)
                .password(password)
                .build();

        TokenResponse tokenResponse = authService.login(logInRequest);

        TokenRequest tokenRequest = TokenRequest.builder()
                .accessToken(tokenResponse.getAccessToken())
                .refreshToken(tokenResponse.getRefreshToken())
                .build();

        // when
        TokenResponse reissueToken = authService.reissueToken(tokenRequest);

        // then
        assertThat(reissueToken.getAccessToken()).isNotNull();
        assertThat(reissueToken.getRefreshToken()).isNotNull();
        assertThat(reissueToken.getGrantType()).isNotNull();
        assertThat(reissueToken.getAccessTokenExpiresIn()).isNotNull();

        String accessToken = reissueToken.getAccessToken();

        assertThat(jwtUtilizer.validateToken(accessToken)).isTrue();

        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
        long memberId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByLoginId(loginId).get();
        assertThat(member.getId()).isEqualTo(memberId);
    }
}
