package com.FlagHome.backend.v1.auth;

import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.v1.auth.dto.LoginRequest;
import com.FlagHome.backend.v1.auth.dto.SignUpRequest;
import com.FlagHome.backend.v1.auth.dto.SignUpResponse;
import com.FlagHome.backend.v1.auth.service.AuthService;
import com.FlagHome.backend.v1.member.Major;
import com.FlagHome.backend.v1.member.Role;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.token.dto.TokenRequest;
import com.FlagHome.backend.v1.token.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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
    private JwtUtilizer jwtUtilizer;

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() {
        // given
        String loginId = "gmlwh124";

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(loginId)
                .password("1234")
                .name("문희조")
                .major(Major.컴퓨터SW)
                .studentId("19017041")
                .build();

        // when
        SignUpResponse signUpResponse = authService.signUp(signUpRequest);

        // then : authservice를 통해서 loginId가 정상적으로 저장되었는지
        Member member = memberRepository.findByLoginId(signUpRequest.getLoginId()).get();
        assertThat(member.getLoginId()).isEqualTo(loginId);
        assertThat(member.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest() {
        // given
        String loginId = "gmlwh124";
        String password = "1234";

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(loginId)
                .password(password)
                .name("문희조")
                .major(Major.컴퓨터SW)
                .studentId("19017041")
                .build();

        Member signUpMember = signUpRequest.toMember(passwordEncoder);
        memberRepository.saveAndFlush(signUpMember);

        LoginRequest logInRequest = LoginRequest.builder()
                .loginID(loginId)
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

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .loginId(loginId)
                .password(password)
                .name("문희조")
                .major(Major.컴퓨터SW)
                .studentId("19017041")
                .build();

        Member signUpMember = signUpRequest.toMember(passwordEncoder);
        memberRepository.save(signUpMember);

        LoginRequest logInRequest = LoginRequest.builder()
                .loginID(loginId)
                .password(password)
                .build();

        TokenResponse loginToken = authService.login(logInRequest);
        TokenRequest tokenRequest = TokenRequest.builder()
                .accessToken(loginToken.getAccessToken())
                .refreshToken(loginToken.getRefreshToken())
                .build();

        // when
        TokenResponse reissueToken = authService.reissueToken(tokenRequest);

        // then : 정상적으로 재발급되는 지, 유효한 지, 데이터가 일치하는 지
        assertThat(reissueToken.getAccessToken()).isNotNull();
        assertThat(reissueToken.getRefreshToken()).isNotNull();
        assertThat(reissueToken.getGrantType()).isEqualTo("Bearer");
        assertThat(reissueToken.getAccessTokenExpiresIn()).isNotNull();

        String accessToken = reissueToken.getAccessToken();

        Authentication authentication = jwtUtilizer.getAuthentication(accessToken);
        long memberId = Long.parseLong(authentication.getName());
        Member member = memberRepository.findByLoginId(loginId).get();
        assertThat(member.getId()).isEqualTo(memberId);
    }
}
