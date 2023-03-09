package com.FlagHome.backend.domain.auth.service;

import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.auth.dto.JoinRequest;
import com.FlagHome.backend.domain.auth.dto.JoinResponse;
import com.FlagHome.backend.domain.auth.dto.SignUpResponse;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.domain.token.service.RefreshTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.utility.InputValidator;
import com.FlagHome.backend.global.utility.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;
    private final MemberService memberService;
    private final AuthRepository authRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtilizer jwtUtilizer;
    private final InputValidator inputValidator;

    public Boolean validateDuplicateLoginId(String loginId) {
        return memberService.isExistLoginId(loginId);
    }

    public Boolean validateEmail(String email) {
        inputValidator.validateUSWEmail(email);
        return memberService.isExistEmail(email);
    }

    @Transactional
    public JoinResponse join(JoinRequest joinRequest) {
        inputValidator.validatePassword(joinRequest.getPassword());
        String certificationNumber = RandomGenerator.getRandomNumber();

        authRepository.save(AuthInformation.of(joinRequest, certificationNumber));
        mailService.sendCertification(joinRequest.getEmail(), certificationNumber);

        return JoinResponse.from(joinRequest.getEmail());
    }

    @Transactional
    public SignUpResponse signUp(String email, String certification) {
        AuthInformation authInformation = findLatestAuthInformationByEmail(email);
        authInformation.validateAuthTime();
        inputValidator.validateCertification(certification, authInformation.getCertification());

        if (authInformation.getJoinType() == JoinType.동아리) {
            authInformation.authorized();
            return SignUpResponse.from(authInformation);
        }

        memberService.initMember(authInformation);
        authRepository.delete(authInformation);
        return SignUpResponse.from(authInformation);
    }

    @Transactional
    public TokenResponse login(String loginId, String password) {
        Member member = memberService.convertSleepingIfExist(loginId);

        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        log.info("token : " + authenticationToken);

        // 실제로 검증(비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("authentication : " + authentication);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);

        // 마지막 로그인 시간 갱신
        member.updateLastLoginTime(LocalDateTime.now());

        // RefreshToken 저장
        refreshTokenService.issueToken(authentication.getName(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    @Transactional
    public TokenResponse reissueToken(String accessToken, String refreshToken) {
        return refreshTokenService.reissueToken(accessToken, refreshToken);
    }

    private AuthInformation findLatestAuthInformationByEmail(String email) {
        return authRepository.findFirstByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INFORMATION_NOT_FOUND));
    }
}
