package com.FlagHome.backend.domain.auth;

import com.FlagHome.backend.domain.auth.dto.JoinRequest;
import com.FlagHome.backend.domain.auth.dto.LoginRequest;
import com.FlagHome.backend.domain.auth.dto.SignUpResponse;
import com.FlagHome.backend.domain.auth.service.AuthService;
import com.FlagHome.backend.domain.member.Major;
import com.FlagHome.backend.domain.member.Role;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.domain.token.service.RefreshTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
public class AuthServiceTest {
    private final String baseUri = "/api/auth";
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtilizer jwtUtilizer;

    @Nested
    @DisplayName("아이디 유효성 테스트")
    class validateIdTest {
        @Test
        @DisplayName("아이디가 중복이 아님")
        void validateIdSuccessTest() {
            String loginId = "gmlwh124";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(password)
                    .build());

            assertThatNoException()
                    .isThrownBy(() -> authService.validateDuplicateLoginId("hejow124"));
        }

        @Test
        @DisplayName("아이디가 중복되어 실패")
        void validateIdFailTest() {
            String loginId = "gmlwh124";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .password(password)
                    .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.validateDuplicateLoginId(loginId))
                    .withMessage("이미 존재하는 아이디 입니다.");
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
            String loginId = "gmlwh124";
            String email = "gmlwh124@naver.com";
            String password = "1234";

            memberRepository.save(Member.builder()
                    .loginId(loginId)
                    .email(email)
                    .password(password)
                    .build());

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.validateEmail(email))
                    .withMessage("수원대학교 웹 메일 주소가 아닙니다.");
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
                    .withMessage("이미 가입된 이메일 입니다.");
        }
    }

//    @Test
//    @DisplayName("회원가입 테스트")
//    void signUpTest() {
//
//    }

//    @Test
//    @DisplayName("로그인 테스트")
//    void loginTest() {
//        // given
//        String loginId = "gmlwh124";
//        String password = "1234";
//
//        JoinRequest joinRequest = JoinRequest.builder()
//                .loginId(loginId)
//                .password(password)
//                .name("문희조")
//                .major(Major.컴퓨터SW)
//                .studentId("19017041")
//                .build();
//
//        Member signUpMember = joinRequest.toMember(passwordEncoder);
//        memberRepository.saveAndFlush(signUpMember);
//
//        LoginRequest logInRequest = LoginRequest.builder()
//                .loginID(loginId)
//                .password(password)
//                .build();
//
//        // when
//        TokenResponse tokenResponse = authService.login(logInRequest);
//
//        // then : 정상적으로 발급되는 지, 유효한 지, 데이터가 일치하는 지
//        assertThat(tokenResponse.getAccessToken()).isNotNull();
//        assertThat(tokenResponse.getRefreshToken()).isNotNull();
//        assertThat(tokenResponse.getAccessTokenExpiresIn()).isNotNull();
//        assertThat(tokenResponse.getGrantType()).isEqualTo("Bearer");
//
//        String accessToken = tokenResponse.getAccessToken();
//
//        assertThat(jwtUtilizer.validateToken(accessToken)).isTrue();
//
//        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
//        long memberId = Long.parseLong(authentication.getName());
//        Member member = memberRepository.findByLoginId(loginId).get();
//        assertThat(member.getId()).isEqualTo(memberId);
//    }

//    @Test
//    @DisplayName("토큰 재발급 테스트")
//    void reIssueToken() {
//        // given
//        String loginId = "gmlwh124";
//        String password = "1234";
//
//        JoinRequest joinRequest = JoinRequest.builder()
//                .loginId(loginId)
//                .password(password)
//                .name("문희조")
//                .major(Major.컴퓨터SW)
//                .studentId("19017041")
//                .build();
//
//        Member signUpMember =
//                joinRequest.toMember(passwordEncoder);
//        memberRepository.save(signUpMember);
//
//        LoginRequest logInRequest = LoginRequest.builder()
//                .loginID(loginId)
//                .password(password)
//                .build();
//
//        TokenResponse tokenResponse = authService.login(logInRequest);
//
//        TokenRequest tokenRequest = TokenRequest.builder()
//                .accessToken(tokenResponse.getAccessToken())
//                .refreshToken(tokenResponse.getRefreshToken())
//                .build();
//
//        // when
//        TokenResponse reissueToken = authService.reissueToken(tokenRequest);
//
//        // then
//        assertThat(reissueToken.getAccessToken()).isNotNull();
//        assertThat(reissueToken.getRefreshToken()).isNotNull();
//        assertThat(reissueToken.getGrantType()).isNotNull();
//        assertThat(reissueToken.getAccessTokenExpiresIn()).isNotNull();
//
//        String accessToken = reissueToken.getAccessToken();
//
//        assertThat(jwtUtilizer.validateToken(accessToken)).isTrue();
//
//        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
//        long memberId = Long.parseLong(authentication.getName());
//        Member member = memberRepository.findByLoginId(loginId).get();
//        assertThat(member.getId()).isEqualTo(memberId);
//    }
}
