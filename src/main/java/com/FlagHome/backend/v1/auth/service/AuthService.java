package com.FlagHome.backend.v1.auth.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.v1.auth.dto.LoginRequest;
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

    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        if (memberRepository.existsByLoginId(signUpRequest.getLoginId())) {
            throw new CustomException(ErrorCode.USER_ID_EXISTS);
        }

        Member member = signUpRequest.toMember(passwordEncoder);
        memberRepository.save(member);
        return SignUpResponse.of(member);
    }

    public TokenResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);
        refreshTokenService.issueToken(authentication.getName(), tokenResponse.getRefreshToken());
        return tokenResponse;
    }

    public TokenResponse reissueToken(TokenRequest tokenRequest) {
        return refreshTokenService.reissueToken(tokenRequest);
    }
}
