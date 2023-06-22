package com.Flaground.backend.module.auth.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.global.exception.domain.CustomBadCredentialException;
import com.Flaground.backend.global.jwt.JwtUtilizer;
import com.Flaground.backend.infra.aws.ses.service.AwsSESService;
import com.Flaground.backend.module.auth.controller.dto.response.SignUpRequestResponse;
import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.domain.repository.AuthRepository;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.service.BlackListService;
import com.Flaground.backend.module.member.service.MemberService;
import com.Flaground.backend.module.token.dto.TokenResponse;
import com.Flaground.backend.module.token.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final BlackListService blackListService;
    private final AwsSESService mailService;
    private final JwtUtilizer jwtUtilizer;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public List<SignUpRequestResponse> getSignUpRequests() {
        return authRepository.getSignUpRequests();
    }

    public boolean validateDuplicateLoginId(String loginId) {
        return memberService.isExistLoginId(loginId);
    }

    public boolean validateDuplicateEmail(String email) {
        blackListService.validateBlackList(email);
        return memberService.isExistEmail(email);
    }

    @Transactional
    public String join(AuthInformation authInformation) {
        validateLoginIdAndEmail(authInformation.getLoginId(), authInformation.getEmail());
        authRepository.save(authInformation);
        mailService.sendCertification(authInformation.getEmail(), authInformation.getCertification());
        return authInformation.getEmail();
    }

    @Transactional
    public AuthInformation signUp(String email, String certification) {
        AuthInformation authInformation = findLatestAuthInformationByEmail(email);
        authInformation.validateCertification(certification);

        if (authInformation.isCrewJoin()) {
            authInformation.authorize();
            return authInformation;
        }

        memberService.initMember(authInformation.toJoinMember());
        return authInformation;
    }

    @Transactional
    public TokenResponse login(String loginId, String password) {
        Member member = memberService.reactivateIfSleeping(loginId);
        member.isLoginnable();

        Authentication authentication = authenticate(loginId, password);
        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);
        refreshTokenService.issueToken(authentication.getName(), tokenResponse.getRefreshToken());

        member.updateLoginTime();
        return tokenResponse;
    }

    @Transactional
    public TokenResponse reissueToken(String accessToken, String refreshToken) {
        return refreshTokenService.reissueToken(accessToken, refreshToken);
    }

    @Transactional
    public void deleteJoinRequest(Long id) {
        authRepository.deleteById(id);
    }

    public AuthInformation findById(Long id) {
        return authRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INFORMATION_NOT_FOUND));
    }

    private void validateLoginIdAndEmail(String loginId, String email) {
        if (isDuplicated(loginId, email)) {
            throw new CustomException(ErrorCode.VALIDATE_NOT_PROCEED);
        }
    }

    private boolean isDuplicated(String loginId, String email) {
        return memberService.isExistLoginId(loginId) && memberService.isExistEmail(email);
    }

    private AuthInformation findLatestAuthInformationByEmail(String email) {
        return authRepository.findFirstByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INFORMATION_NOT_FOUND));
    }

    private Authentication authenticate(String loginId, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
            return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            int failCount = memberService.loginFailed(loginId);
            throw new CustomBadCredentialException(ErrorCode.PASSWORD_NOT_MATCH, failCount);
        }
    }
}
