package com.Flaground.backend.module.member.service;

import com.Flaground.backend.global.common.response.SearchResponse;
import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.global.utility.RandomGenerator;
import com.Flaground.backend.infra.aws.s3.entity.enums.FileDirectory;
import com.Flaground.backend.infra.aws.s3.service.AwsS3ServiceImpl;
import com.Flaground.backend.infra.aws.ses.service.AwsSESServiceImpl;
import com.Flaground.backend.module.member.controller.dto.response.*;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.JoinMember;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.token.domain.Token;
import com.Flaground.backend.module.token.service.RecoveryTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RecoveryTokenService recoveryTokenService;
    private final SleepingService sleepingService;
    private final AwsSESServiceImpl mailService;
    private final AwsS3ServiceImpl awsS3Service;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AvatarResponse getMemberPageAvatar(String loginId) {
        Member member = findByLoginId(loginId);
        member.isWithdraw();
        return AvatarResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MyProfileResponse getMyProfile(Long memberId) {
        return memberRepository.getMyProfile(memberId);
    }

    @Transactional(readOnly = true)
    public List<LoginLogResponse> getLoginLogs() {
        return memberRepository.getLoginLogs();
    }

    @Transactional(readOnly = true)
    public SearchResponse<SearchMemberResponse> searchMember(String name) {
        return memberRepository.searchMemberByName(name);
    }

    @Transactional(readOnly = true)
    public boolean isExistLoginId(String loginId) {
        return memberRepository.existsByLoginId(loginId) || sleepingService.existsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public boolean isExistEmail(String email) {
        return memberRepository.existsByEmail(email) || sleepingService.existsByEmail(email);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int loginFailed(String loginId) {
        Member member = findByLoginId(loginId);
        member.loginFail();
        return member.getFailCount();
    }

    public void withdraw(Long memberId, String password) {
        Member member = validatePasswordAndReturn(memberId, password);
        member.withdraw();
    }

    public RecoveryResponse findId(String name, String email) {
        Member member = findByEmail(email);
        member.validateName(name);
        return issueTokenAndSendMail(email);
    }

    public RecoveryResponse findPassword(String loginId, String email) {
        Member member = findByEmail(email);
        member.validateLoginId(loginId);
        return issueTokenAndSendMail(email);
    }

    // todo: 아아디/비밀번호 찾기에 대해 분리할지 고민
    public RecoveryResultResponse validateCertification(String email, String certification) {
        Token findRequestToken = recoveryTokenService.findToken(email);
        findRequestToken.validateCertification(certification);
        Member member = findByEmail(email);
        return RecoveryResultResponse.of(member.getLoginId(), email);
    }

    public void changePassword(String email, String newPassword) {
        Member member = findByEmail(email);
        member.updatePassword(newPassword, passwordEncoder);
    }

    public void updatePassword(Long memberId, String currentPassword, String newPassword) {
        Member member = validatePasswordAndReturn(memberId, currentPassword);
        member.validateSamePassword(newPassword, passwordEncoder);
        member.updatePassword(newPassword, passwordEncoder);
    }

    public void initMember(JoinMember joinMember) {
        memberRepository.save(joinMember.toMember(passwordEncoder));
    }

    public Member reactivateIfSleeping(String loginId) {
        sleepingService.reactiveIfSleeping(loginId);
        return findByLoginId(loginId);
    }

    public void updateAvatar(Long memberId, Avatar avatar) {
        Member member = findById(memberId);
        member.getAvatar().updateAvatar(avatar);
    }

    public void updateProfileImage(Long memberId, MultipartFile file) {
        Member member = findById(memberId);
        String profileImageUrl = awsS3Service.upload(file, FileDirectory.PROFILE.getDirectory());
        member.changeProfileImage(profileImageUrl);
    }

    public String resetProfileImage(Long memberId) {
        Member member = findById(memberId);
        return member.resetProfileImage();
    }

    public List<Member> getMembersByLoginIds(List<String> loginIds) {
        return memberRepository.findByLoginIdIn(loginIds);
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Member validatePasswordAndReturn(Long memberId, String password) {
        Member member = findById(memberId);
        member.validatePassword(password, passwordEncoder);
        return member;
    }

    private RecoveryResponse issueTokenAndSendMail(String email) {
        String certification = RandomGenerator.getRandomNumber();
        mailService.sendFindCertification(email, certification);
        Token findRequestToken = recoveryTokenService.issueToken(email, certification);
        return RecoveryResponse.of(email, findRequestToken.getExpiredAt());
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
