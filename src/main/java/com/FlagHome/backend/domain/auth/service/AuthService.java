package com.FlagHome.backend.domain.auth.service;

import com.FlagHome.backend.domain.auth.JoinType;
import com.FlagHome.backend.domain.auth.dto.JoinRequest;
import com.FlagHome.backend.domain.auth.dto.JoinResponse;
import com.FlagHome.backend.domain.auth.dto.SignUpResponse;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.domain.member.avatar.service.AvatarService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.token.dto.TokenResponse;
import com.FlagHome.backend.domain.token.service.RefreshTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.jwt.JwtUtilizer;
import com.FlagHome.backend.global.utility.InputValidator;
import com.FlagHome.backend.global.utility.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final AuthRepository authRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilizer jwtUtilizer;
    private final InputValidator inputValidator;
    private final AvatarService avatarService;

    public Boolean validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public Boolean validateEmail(String email) {
        inputValidator.validateUSWEmail(email);

        if (memberRepository.existsByEmail(email)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
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
        AuthInformation authInformation = authRepository.findFirstByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INFORMATION_NOT_FOUND));

        inputValidator.validateCertification(certification, authInformation.getCertification());

        // 동아리원이면 인증 상태를 업데이트하고 이후 관리자의 확인을 받는다.
        if (authInformation.getJoinType() == JoinType.동아리) {
            authInformation.updateAuthorizedTrue();
            return SignUpResponse.from(authInformation);
        }

        Member member = memberRepository.save(Member.of(authInformation, passwordEncoder));
        avatarService.initAvatar(member, authInformation.getNickName());
        authRepository.delete(authInformation);
        return SignUpResponse.from(authInformation);
    }

    @Transactional
    public TokenResponse login(String loginId, String password) {
        // Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);;

        // 실제로 검증(비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenResponse = jwtUtilizer.generateTokenDto(authentication);

        // 마지막 로그인 시간 갱신
        Member member = memberService.findByLoginId(loginId);
        member.updateLastLoginTime(LocalDateTime.now());

        // RefreshToken 저장
        refreshTokenService.issueToken(authentication.getName(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

    @Transactional
    public TokenResponse reissueToken(String accessToken, String refreshToken) {
        return refreshTokenService.reissueToken(accessToken, refreshToken);
    }
}
