package com.FlagHome.backend.module.auth.service;

import com.FlagHome.backend.module.auth.domain.AuthInformation;
import com.FlagHome.backend.module.auth.controller.dto.response.JoinResponse;
import com.FlagHome.backend.module.auth.controller.dto.response.SignUpResponse;
import com.FlagHome.backend.module.auth.domain.repository.AuthRepository;
import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.member.service.MemberService;
import com.FlagHome.backend.module.token.dto.TokenResponse;
import com.FlagHome.backend.module.token.service.RefreshTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.infra.aws.ses.service.MailService;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.utility.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;
    private final JwtUtilizer jwtUtilizer;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Boolean validateDuplicateLoginId(String loginId) {
        return memberService.isExistLoginId(loginId);
    }

    public Boolean validateDuplicateEmail(String email) {
        return memberService.isExistEmail(email);
    }

    public String join(AuthInformation authInformation) {
        isDuplicateValidated(authInformation.getLoginId(), authInformation.getEmail());

        authRepository.save(authInformation);
        mailService.sendCertification(authInformation.getEmail(), authInformation.getCertification());

        return authInformation.getEmail();
    }

    public AuthInformation signUp(String email, String certification) {
        AuthInformation authInformation = findLatestAuthInformationByEmail(email);
        authInformation.validateAuthTime();
        authInformation.validateCertification(certification);

        if (authInformation.isCrewJoin()) {
            authInformation.authorized();
            return authInformation;
        }

        memberService.initMember(authInformation);
        return authInformation;
    }

    public TokenResponse login(String loginId, String password) {
        Member member = memberService.reactivateIfSleeping(loginId);
        member.isAvailable();

        Authentication authentication = authenticate(loginId, password);
        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);
        refreshTokenService.issueToken(authentication.getName(), tokenResponse.getRefreshToken());

        member.updateLoginTime();
        return tokenResponse;
    }

    public TokenResponse reissueToken(String accessToken, String refreshToken) {
        return refreshTokenService.reissueToken(accessToken, refreshToken);
    }

    private void isDuplicateValidated(String loginId, String email) {
        if (validateDuplicateLoginId(loginId) || validateDuplicateEmail(email)) {
            throw new CustomException(ErrorCode.VALIDATE_NOT_PROCEED);
        }
    }

    private AuthInformation findLatestAuthInformationByEmail(String email) {
        return authRepository.findFirstByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INFORMATION_NOT_FOUND));
    }

    private Authentication authenticate(String loginId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }
}
