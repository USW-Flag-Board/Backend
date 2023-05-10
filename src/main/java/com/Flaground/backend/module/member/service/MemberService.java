package com.Flaground.backend.module.member.service;

import com.Flaground.backend.global.exception.CustomException;
import com.Flaground.backend.global.exception.ErrorCode;
import com.Flaground.backend.global.utility.RandomGenerator;
import com.Flaground.backend.infra.aws.s3.entity.enums.FileDirectory;
import com.Flaground.backend.infra.aws.s3.service.AwsS3Service;
import com.Flaground.backend.infra.aws.ses.service.MailService;
import com.Flaground.backend.module.member.controller.dto.response.AvatarResponse;
import com.Flaground.backend.module.member.controller.dto.response.LoginLogResponse;
import com.Flaground.backend.module.member.controller.dto.response.MyProfileResponse;
import com.Flaground.backend.module.member.controller.dto.response.SearchMemberResponse;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.JoinMember;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.token.domain.Token;
import com.Flaground.backend.module.token.service.RecoveryTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    private final MailService mailService;
    private final AwsS3Service awsS3Service;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AvatarResponse getMemberPageAvatar(String loginId) {
        Member member = findByLoginId(loginId);
        member.isAvailable();
        return memberRepository.getAvatar(loginId);
    }

    @Transactional(readOnly = true)
    public MyProfileResponse getMyProfile(Long memberId) {
        return memberRepository.getMyProfile(memberId);
    }

    @Transactional(readOnly = true)
    public List<LoginLogResponse> getAllLoginLogs() {
        return memberRepository.getAllLoginLogs();
    }

    @Transactional(readOnly = true)
    public List<SearchMemberResponse> searchMember(String name) {
        return memberRepository.searchMemberByName(name);
    }

    public Boolean isExistLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId) || sleepingService.existsByLoginId(loginId)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean isExistEmail(String email) {
        if (memberRepository.existsByEmail(email) || sleepingService.existsByEmail(email)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void withdraw(Long memberId, String password) {
        Member member = verifyPasswordAndReturn(memberId, password);
        member.withdraw();
    }

    public Token findId(String name, String email) {
        Member member = findByEmail(email);
        validateNameAndEmailMatches(member.getName(), name);
        return issueTokenAndSendMail(email);
    }

    public Token findPassword(String loginId, String email) {
        Member member = findByEmail(email);
        validateLoginIdAndEmailMatches(member.getLoginId(), loginId);
        return issueTokenAndSendMail(email);
    }

    public Member verifyCertification(String email, String certification) {
        Token findRequestToken = recoveryTokenService.findToken(email);
        findRequestToken.validateExpireTime();
        findRequestToken.verifyCertification(certification);
        return findByEmail(email);
    }

    public void changePassword(String email, String newPassword) {
        Member member = findByEmail(email);
        member.updatePassword(newPassword, passwordEncoder);
    }

    public void updatePassword(Long memberId, String currentPassword, String newPassword) {
        Member member = verifyPasswordAndReturn(memberId, currentPassword);
        validatePassword(newPassword, member.getPassword());
        member.updatePassword(newPassword, passwordEncoder);
    }

    public void initMember(JoinMember joinMember) {
        memberRepository.save(joinMember.toMember(passwordEncoder));
    }

    public Member reactivateIfSleeping(String loginId) {
        sleepingService.reactivateMember(loginId);
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

    public void resetProfileImage(Long memberId) {
        Member member = findById(memberId);
        member.resetProfileImage();
    }

    public List<Member> getMembersByLoginIds(List<String> loginIdList) {
        return memberRepository.getMembersByLoginIds(loginIdList);
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Member verifyPasswordAndReturn(Long memberId, String password) {
        Member member = findById(memberId);
        verifyPassword(password, member.getPassword());
        return member;
    }

    private Token issueTokenAndSendMail(String email) {
        String certification = RandomGenerator.getRandomNumber();
        Token findRequestToken = recoveryTokenService.issueToken(email, certification);
        mailService.sendFindCertification(email, certification);
        return findRequestToken;
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private boolean isSamePassword(String input, String saved) {
        return passwordEncoder.matches(input, saved);
    }

    private void verifyPassword(String input, String saved) {
        if (!isSamePassword(input, saved)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    private void validatePassword(String newPassword, String currentPassword) {
        if (isSamePassword(newPassword, currentPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }
    }

    private void validateNameAndEmailMatches(String saved, String input) {
        if (!StringUtils.equals(saved, input)) {
            throw new CustomException(ErrorCode.EMAIL_NAME_NOT_MATCH);
        }
    }

    private void validateLoginIdAndEmailMatches(String saved, String input) {
        if (!StringUtils.equals(saved, input)) {
            throw new CustomException(ErrorCode.EMAIL_ID_NOT_MATCH);
        }
    }
}