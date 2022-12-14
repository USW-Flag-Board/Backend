package com.FlagHome.backend.domain.auth.service;

import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.auth.dto.*;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.token.dto.TokenRequest;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.domain.token.service.RefreshTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.utility.RandomGenerator;
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
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilizer jwtUtilizer;

    public void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.LOGIN_ID_EXISTS);
        }
    }

    public void validateEmail(String email) {
        if (!StringUtils.contains(email, "@")) {
            throw new CustomException(ErrorCode.NOT_EMAIL);
        }

        validateUSWEmail(email);

        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }

    @Transactional
    public void join(JoinRequest joinRequest) {
        validatePassword(joinRequest.getPassword());

        String certificationNumber = RandomGenerator.getRandomNumber();
        AuthInformation authInformation = AuthInformation.of(joinRequest, certificationNumber);
        authRepository.save(authInformation);
    }

    public JoinResponse sendCertification(String email) {
        AuthInformation authInformation = findByEmail(email);
        mailService.sendCertification(email, authInformation.getCertification());

        return JoinResponse.from(authInformation);
    }

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        AuthInformation authInformation = findByEmail(signUpRequest.getEmail());

        validateCertification(signUpRequest.getCertification(), authInformation.getCertification());

        // ?????????????????? ?????? ????????? ??????????????????.
        if (authInformation.getJoinType() == JoinType.?????????) {
            authInformation.updateAuthorizedTrue();
            return SignUpResponse.from(authInformation);
        }

        memberRepository.save(Member.of(authInformation, passwordEncoder));
        return SignUpResponse.from(authInformation);
    }

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        // Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

        // ????????? ??????(???????????? ??????)??? ??????????????? ??????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // ?????? ????????? ???????????? JWT ?????? ??????
        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);

        // RefreshToken ??????
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

    private AuthInformation findByEmail(String email) {
        return authRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_TARGET_NOT_FOUND));
    }
}
