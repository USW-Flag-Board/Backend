package com.FlagHome.backend.domain.auth;

import com.FlagHome.backend.domain.auth.controller.dto.JoinRequest;
import com.FlagHome.backend.domain.auth.controller.dto.SignUpResponse;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.auth.service.AuthService;
import com.FlagHome.backend.domain.member.Role;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.member.sleeping.repository.SleepingRepository;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.utility.RandomGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SleepingRepository sleepingRepository;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("아이디 유효성 테스트")
    void validateLoginIdTest() {
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

    @Nested
    @DisplayName("이메일 유효성 테스트")
    class validateEmailTest {
        @Test
        @DisplayName("이메일 유효성 감사 성공")
        void validateEmailSuccessTest() {
            // given
            String email = "gmlwh124@suwon.ac.kr";
            String noneEmail = "hejow124@suwon.ac.kr";

            memberRepository.save(Member.builder().email(email).build());
            // when
            boolean shouldBeTrue = authService.validateEmail(email);
            boolean shouldBeFalse = authService.validateEmail(noneEmail);

            // them
            assertThat(shouldBeTrue).isTrue();
            assertThat(shouldBeFalse).isFalse();
        }

        @Test
        @DisplayName("수원대 이메일이 아니라 실패")
        void validateUSWEmailFailTest() {
            String email = "gmlwh124@naver.com";

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.validateEmail(email))
                    .withMessage(ErrorCode.NOT_USW_EMAIL.getMessage());
        }
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
            JoinType joinType = JoinType.일반;
            String certification = RandomGenerator.getRandomNumber();

            JoinRequest joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .joinType(joinType)
                    .build();

            AuthInformation authInformation = AuthInformation.of(joinRequest, certification);
            authRepository.saveAndFlush(authInformation);

            // when
            SignUpResponse signUpResponse = authService.signUp(email, certification);

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
                    .joinType(JoinType.동아리)
                    .build();

            AuthInformation authInformation = AuthInformation.of(joinRequest, certification);
            authRepository.saveAndFlush(authInformation);

            // when
            SignUpResponse signUpResponse = authService.signUp(email, certification);

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

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.signUp(email, wrongCertification))
                    .withMessage(ErrorCode.CERTIFICATION_NOT_MATCH.getMessage());
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class loginTest {
        @Test
        @DisplayName("로그인 테스트")
        void loginSuccessTest() {
            // given
            String loginId = "gmlwh124";
            String password = "1234";

            Member savedMember = memberRepository.saveAndFlush(Member.builder()
                            .loginId(loginId)
                            .password(passwordEncoder.encode(password))
                            .role(Role.ROLE_USER)
                            .build());

            // when
            TokenResponse tokenResponse = authService.login(loginId, password);
            entityManager.clear();

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
            assertThat(savedMember.getLastLoginTime()).isNotEqualTo(member.getLastLoginTime());
            assertThat(member.getId()).isEqualTo(memberId);
        }

        @Test
        @DisplayName("로그인 실패 테스트")
        void loginFailTest() {
            String loginId = "gmlwh124";
            String password = "qwer1234!";
            String wrongPassword = "wert2345@";

            Member member = Member.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .role(Role.ROLE_USER)
                    .build();

            memberRepository.save(member);

            assertThatExceptionOfType(BadCredentialsException.class)
                    .isThrownBy(() -> authService.login(loginId, wrongPassword));
        }
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
        Member member = memberRepository.findByLoginId(loginId).get();
        assertThat(member.getId()).isEqualTo(memberId);
    }

//    @Test
//    @DisplayName("휴면계정 확인 테스트")
//    void checkSleepingTest() {
//        //given
//        String loginId = "hwyoung123";
//        LocalDateTime lastLoginTime = LocalDateTime.now().minusDays(7);
//        Status status = Status.GENERAL;
//
//        Member member = memberRepository.save(Member.builder()
//                .loginId(loginId)
//                .lastLoginTime(lastLoginTime)
//                .status(status)
//                .build());
//
//        memberService.changeAllToSleepMember();
//
////        Sleeping sleeping = sleepingRepository.save(Sleeping.builder()
////                .loginId(loginId)
////                .build());
//
//        //when
//
//        authService.checkSleeping(loginId);
//
//        //then
//        assertThat(member.getStatus()).isEqualTo(Status.GENERAL);
//
//
//    }
}
