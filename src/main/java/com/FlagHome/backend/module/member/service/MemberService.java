package com.FlagHome.backend.module.member.service;

import com.FlagHome.backend.module.auth.domain.AuthInformation;
import com.FlagHome.backend.module.member.controller.dto.response.*;
import com.FlagHome.backend.module.member.domain.Avatar;
import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.member.domain.repository.MemberRepository;
import com.FlagHome.backend.module.member.domain.Sleeping;
import com.FlagHome.backend.module.token.entity.Token;
import com.FlagHome.backend.module.token.service.FindRequestTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.utility.RandomGenerator;
import com.FlagHome.backend.infra.aws.s3.entity.enums.FileDirectory;
import com.FlagHome.backend.infra.aws.s3.service.AwsS3Service;
import com.FlagHome.backend.infra.aws.ses.service.MailService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final AwsS3Service awsS3Service;
    private final FindRequestTokenService findRequestTokenService;
    private final PasswordEncoder passwordEncoder;
    private final SleepingService sleepingService;

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
    public List<SearchMemberResponse> searchByMemberName(String name) {
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

    public AccountRecoveryResponse findId(String name, String email) {
        Member member = findByEmail(email);
        validateMemberName(member.getName(), name);
        return issueTokenAndSendMail(email);
    }

    public AccountRecoveryResponse findPassword(String loginId, String email) {
        Member member = findByEmail(email);
        validateMemberLoginId(member.getLoginId(), loginId);
        return issueTokenAndSendMail(email);
    }

    public AccountRecoveryResultResponse verifyCertification(String email, String certification) {
        Token findRequestToken = findRequestTokenService.findToken(email);
        findRequestToken.validateExpireTime();
        findRequestToken.verifyCertification(certification);
        Member member = findByEmail(email);
        return AccountRecoveryResultResponse.from(member);
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

    public void initMember(AuthInformation authInformation) {
        memberRepository.save(Member.of(authInformation, passwordEncoder));
    }

    public Member reactivateIfSleeping(String loginId) {
        Sleeping sleeping = sleepingService.findByLoginId(loginId);

        if (sleeping != null) {
            Member member = findById(sleeping.getMember().getId());
            sleepingService.reactivateMember(member, sleeping);
            return member;
        }

        return findByLoginId(loginId);
    }

    public void updateAvatar(Long memberId, Avatar avatar) {
        Member member = findById(memberId);
        member.getAvatar().updateAvatar(avatar);
    }

    public void updateProfileImage(Long memberId, MultipartFile file) {
        Member member = findById(memberId);
        String profileImageUrl = awsS3Service.upload(file, FileDirectory.PROFILE.getDirectory());
        member.getAvatar().changeProfileImage(profileImageUrl);
    }

    //@Scheduled(cron = "000000")
    public void selectingDeactivateMembers() {
        List<Member> deactivateMembers = memberRepository.getDeactivateMembers();
        List<Sleeping> sleepings = convertToSleepings(deactivateMembers);
        sleepingService.saveAllSleeping(sleepings);
        deactivateMembers(deactivateMembers);
    }

    public List<Member> getMembersByLoginIds(List<String> loginIdList) {
        return memberRepository.getMembersByLoginIds(loginIdList);
    }

    //@Scheduled(cron = "000000")
    public void sendNotificationToDeactivableMembers() {
        List<String> emailLists = memberRepository.getAllBeforeSleepEmails();
        emailLists.forEach(mailService::sendChangeSleep);
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

    private AccountRecoveryResponse issueTokenAndSendMail(String email) {
        String certification = RandomGenerator.getRandomNumber();
        Token findRequestToken = findRequestTokenService.issueToken(email, certification);
        mailService.sendFindCertification(email, certification);
        return AccountRecoveryResponse.from(findRequestToken);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private List<Sleeping> convertToSleepings(List<Member> deactivateMembers) {
        return deactivateMembers.stream()
                .map(Sleeping::of)
                .collect(Collectors.toList());
    }

    private void deactivateMembers(List<Member> memberList) {
        memberList.forEach(Member::deactivate);
    }

    private boolean isPasswordMatches(String input, String saved) {
        return passwordEncoder.matches(input, saved);
    }

    private void verifyPassword(String input, String saved) {
        if (!isPasswordMatches(input, saved)) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    private void validatePassword(String newPassword, String currentPassword) {
        if (isPasswordMatches(newPassword, currentPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }
    }

    private void validateMemberName(String saved, String input) {
        if (!StringUtils.equals(saved, input)) {
            throw new CustomException(ErrorCode.EMAIL_NAME_NOT_MATCH);
        }
    }

    private void validateMemberLoginId(String saved, String input) {
        if (!StringUtils.equals(saved, input)) {
            throw new CustomException(ErrorCode.EMAIL_ID_NOT_MATCH);
        }
    }
}