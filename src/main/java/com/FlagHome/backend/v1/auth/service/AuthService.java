package com.FlagHome.backend.v1.auth.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.v1.auth.dto.LogInRequest;
import com.FlagHome.backend.v1.auth.dto.SignUpRequest;
import com.FlagHome.backend.v1.auth.dto.SignUpResponse;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.token.dto.TokenRequest;
import com.FlagHome.backend.v1.token.dto.TokenResponse;
import com.FlagHome.backend.v1.token.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtilizer jwtUtilizer;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        Member member = signUpRequest.toMember(passwordEncoder);
        memberRepository.save(member);
        return SignUpResponse.of(member);
    }

    @Transactional
    public TokenResponse logIn(LogInRequest logInRequest) {
        // 로그인 id, pw로 Authentication Token 발급
        // 실제로 검증하는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        UsernamePasswordAuthenticationToken authenticationToken = logInRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반한 JWT 토큰 생성
        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);

        // RefreshToken 저장
        refreshTokenService.issueToken(authentication.getName(), tokenResponse.getRefreshToken());

        // 토큰 발급
        return tokenResponse;
    }

    @Transactional
    public TokenResponse reissueToken(TokenRequest tokenRequest) {
        return refreshTokenService.reissueToken(tokenRequest);
    }
}
