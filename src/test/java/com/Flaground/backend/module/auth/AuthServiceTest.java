package com.Flaground.backend.module.auth;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.global.jwt.JwtUtilizer;
import com.Flaground.backend.module.auth.controller.dto.request.JoinRequest;
import com.Flaground.backend.module.auth.controller.dto.response.SignUpResponse;
import com.Flaground.backend.module.auth.controller.mapper.AuthMapper;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.domain.JoinType;
import com.Flaground.backend.module.auth.domain.repository.AuthRepository;
import com.Flaground.backend.module.auth.service.AuthService;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.enums.Role;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.token.dto.TokenResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class AuthServiceTest {
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

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAllInBatch();
    }

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
        private JoinRequest joinRequest;

        private void initRequest(JoinType joinType) {
            joinRequest = JoinRequest.builder()
                    .loginId(loginId)
                    .password(password)
                    .email(email)
                    .joinType(joinType)
                    .build();
        }

        @Test
        void 일반유저_회원가입_테스트() {
            // given
            final JoinType joinType = JoinType.NORMAL;
            initRequest(joinType);

            AuthInformation authInformation = authRepository.saveAndFlush(authMapper.mapFrom(joinRequest));

            // when
            SignUpResponse signUpResponse = SignUpResponse.from(authService.signUp(email, authInformation.getCertification()));

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
            initRequest(joinType);

            AuthInformation authInformation = authRepository.saveAndFlush(authMapper.mapFrom(joinRequest));

            // when
            AuthInformation savedInformation = authService.signUp(email, authInformation.getCertification());

            // then
            SignUpResponse signUpResponse = SignUpResponse.from(savedInformation);
            AuthInformation signupAuthInformation = authRepository.findFirstByEmailOrderByCreatedAtDesc(email).orElse(null);
            assertThat(signUpResponse.getEmail()).isEqualTo(email);
            assertThat(signupAuthInformation).isNotNull();
            assertThat(signupAuthInformation.isAuthorizedCrew()).isTrue();
        }

        @Test
        void 인증번호_오류_테스트() {
            final String wrongCertification = "abcdef";
            final JoinType joinType = JoinType.NORMAL;
            initRequest(joinType);

            authRepository.saveAndFlush(authMapper.mapFrom(joinRequest));

            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(() -> authService.signUp(email, wrongCertification))
                    .withMessage(ErrorCode.CERTIFICATION_NOT_MATCH.getMessage());
        }
    }

    @Test
    void 로그인_성공_테스트() {
        // given
        final String loginId = "gmlwh124";
        final String password = "qwer1234!";
        final Role role = Role.ROLE_USER;

        Member member = memberRepository.save(Member.builder()
                .loginId(loginId)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build());

        // when
        TokenResponse tokenResponse = authService.login(loginId, password);

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getRefreshToken()).isNotNull();
        assertThat(tokenResponse.getAccessTokenExpiresIn()).isNotNull();
        assertThat(tokenResponse.getGrantType()).isEqualTo("Bearer");

        String accessToken = tokenResponse.getAccessToken();
        assertThat(jwtUtilizer.validateToken(accessToken)).isTrue();

        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
        long memberId = Long.parseLong(authentication.getName());
        Member findMember = memberRepository.findByLoginId(loginId).orElse(null);
        assertThat(findMember).isNotNull();
        assertThat(member.getUpdatedAt()).isNotEqualTo(findMember.getUpdatedAt());
        assertThat(findMember.getId()).isEqualTo(memberId);
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
