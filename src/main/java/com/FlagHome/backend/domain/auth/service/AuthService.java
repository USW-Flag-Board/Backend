package com.FlagHome.backend.domain.auth.service;

import com.FlagHome.backend.domain.auth.AuthInformation;
import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.auth.controller.dto.JoinRequest;
import com.FlagHome.backend.domain.auth.controller.dto.JoinResponse;
import com.FlagHome.backend.domain.auth.controller.dto.SignUpResponse;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.domain.token.service.RefreshTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.infra.aws.ses.service.MailService;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.utility.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;
    private final MemberService memberService;
    private final AuthRepository authRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtilizer jwtUtilizer;

    public Boolean validateDuplicateLoginId(String loginId) {
        return memberService.isExistLoginId(loginId);
    }

    public Boolean validateEmail(String email) {
        return memberService.isExistEmail(email);
    }

    @Transactional
    public JoinResponse join(JoinRequest joinRequest) {
        if (validateDuplicateLoginId(joinRequest.getLoginId()) || validateEmail(joinRequest.getEmail())) {
            throw new CustomException(ErrorCode.VALIDATE_NOT_PROCEED);
        }

        String certificationNumber = RandomGenerator.getRandomNumber();

        authRepository.save(AuthInformation.of(joinRequest, certificationNumber));
        mailService.sendCertification(joinRequest.getEmail(), certificationNumber);

        return JoinResponse.from(joinRequest.getEmail());
    }

    @Transactional
    public SignUpResponse signUp(String email, String certification) {
        AuthInformation authInformation = findLatestAuthInformationByEmail(email);
        authInformation.validateAuthTime();
        authInformation.validateCertification(certification);

        if (authInformation.getJoinType() == JoinType.동아리) {
            authInformation.authorized();
            return SignUpResponse.from(authInformation);
        }

        memberService.initMember(authInformation);
        return SignUpResponse.from(authInformation);
    }

    @Transactional
    public TokenResponse login(String loginId, String password) {
        Member member = memberService.convertSleepingIfExist(loginId);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);
        refreshTokenService.issueToken(authentication.getName(), tokenResponse.getRefreshToken());

        member.renewLoginTime();
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
