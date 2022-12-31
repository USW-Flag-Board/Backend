package com.FlagHome.backend.domain.auth.service;

import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.auth.dto.*;
import com.FlagHome.backend.domain.auth.entity.AuthMember;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.mail.MailType;
import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.token.dto.TokenRequest;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.domain.token.service.RefreshTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.util.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtilizer jwtUtilizer;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final AuthRepository authRepository;
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.LOGIN_ID_EXISTS);
        }
    }

    public void validateEmail(String email) {
        validateUSWEmail(email);

        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }

    @Transactional
    public JoinResponse join(JoinRequest joinRequest) {
        validatePassword(joinRequest.getPassword());

        String certificationNumber = RandomGenerator.getRandomNumber();
        AuthMember authMember = AuthMember.of(joinRequest, certificationNumber);

        mailService.sendMailByType(joinRequest.getEmail(), MailType.AUTH_EMAIL, certificationNumber);
        authRepository.save(authMember);

        return JoinResponse.from(authMember);
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        AuthMember authMember = authRepository.findByEmail(signUpRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_TARGET_NOT_FOUND));

        validateCertification(signUpRequest.getCertification(), authMember.getCertification());

        // 동아리원이면 인증 상태를 업데이트한다.
        if (authMember.getJoinType() == JoinType.CLUB) {
            authMember.updateAuthorizedTrue();
            return SignUpResponse.from(authMember);
        }

        memberRepository.save(Member.of(authMember, passwordEncoder));
        return SignUpResponse.from(authMember);
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

        // 실제로 검증(비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);

        // RefreshToken 저장
        refreshTokenService.issueToken(authentication.getName(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    @Transactional
    public TokenResponse reissueToken(TokenRequest tokenRequest) {
        return refreshTokenService.reissueToken(tokenRequest);
    }

    private void validateCertification(String inputCertification, String savedCertification) {
        if (!StringUtils.equals(inputCertification, savedCertification)) {
            throw new CustomException(ErrorCode.CERTIFICATION_NOT_MATCH);
        }
    }

    private void validateUSWEmail(String email) {
        int separateIndex = StringUtils.indexOf(email, "@");
        if (!StringUtils.equals(email.substring(separateIndex), "@suwon.ac.kr")) {
            throw new CustomException(ErrorCode.NOT_USW_EMAIL);
        }
    }

    private void validatePassword(String password) {
        Pattern passwordPattern = Pattern
                .compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$");

        Matcher matcher = passwordPattern.matcher(password);

        if (!matcher.find()) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }
}
