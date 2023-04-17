package com.FlagHome.backend.domain.member.service;

import com.FlagHome.backend.domain.auth.AuthInformation;
import com.FlagHome.backend.domain.member.controller.dto.response.*;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.entity.Avatar;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.member.sleeping.service.SleepingService;
import com.FlagHome.backend.domain.member.token.entity.Token;
import com.FlagHome.backend.domain.member.token.service.FindRequestTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.infra.aws.s3.service.AwsS3Service;
import com.FlagHome.backend.infra.aws.ses.service.MailService;
import com.FlagHome.backend.global.utility.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private static final String PROFILE_IMAGE_DIRECTORY = "/avatar";
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final AwsS3Service awsS3Service;
    private final FindRequestTokenService findRequestTokenService;
    private final PasswordEncoder passwordEncoder;
    private final SleepingService sleepingService;

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

    @Transactional
    public void withdraw(Long memberId, String password) {
        Member member = validatePasswordAndReturn(memberId, password);
        member.withdraw();
    }

    public FindResponse findId(String name, String email) {
        Member member = findByEmail(email);
        if (!StringUtils.equals(member.getName(), name)) {
            throw new CustomException(ErrorCode.EMAIL_NAME_NOT_MATCH);
        }

        return issueTokenAndSendMail(email);
    }

    public FindResponse findPassword(String loginId, String email) {
        Member member = findByEmail(email);

        if (!StringUtils.equals(loginId, member.getLoginId())) {
            throw new CustomException(ErrorCode.EMAIL_ID_NOT_MATCH);
        }

        return issueTokenAndSendMail(email);
    }

    public FindResultResponse verifyCertification(String email, String certification) {
        Token findRequestToken = findRequestTokenService.findToken(email);
        findRequestToken.validateExpireTime();
        findRequestToken.verifyCertification(certification);

        Member member = findByEmail(email);
        return FindResultResponse.from(member);
    }

    @Transactional // 비밀번호를 잊어서 바꾸는 경우
    public void changePassword(String email, String newPassword) {
        Member member = findByEmail(email);
        member.updatePassword(newPassword, passwordEncoder);
    }

    @Transactional // 비밀번호를 유저가 변경하는 경우
    public void updatePassword(Long memberId, String currentPassword, String newPassword) {
        Member member = validatePasswordAndReturn(memberId, currentPassword);

        if (passwordEncoder.matches(newPassword, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }

        member.updatePassword(newPassword, passwordEncoder);
    }

    @Transactional
    public void initMember(AuthInformation authInformation) {
        memberRepository.save(Member.of(authInformation, passwordEncoder));
    }

    @Transactional
    public Member convertSleepingIfExist(String loginId) {
        Sleeping sleeping = sleepingService.findByLoginId(loginId);
        Member member = findByLoginId(loginId);

        if (sleeping != null) {
            sleepingService.convertSleepToMember(member, sleeping);
        }

        return member;
    }

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

    @Transactional
    public void updateAvatar(Long memberId, Avatar avatar) {
        Member member = findById(memberId);
        member.getAvatar().updateAvatar(avatar);
    }

    @Transactional
    public void updateProfileImage(Long memberId, MultipartFile file) {
        Member member = findById(memberId);
        String profileImageUrl =  awsS3Service.upload(file, PROFILE_IMAGE_DIRECTORY);
        member.getAvatar().changeProfileImage(profileImageUrl);
    }

    @Transactional
    //@Scheduled(cron = "000000")
    public void changeAllToSleepMember() {
        List<Member> sleepingMembers = memberRepository.getAllSleepMembers();
        List<Sleeping> sleepingList = sleepingMembers.stream()
                        .map(Sleeping::of)
                        .collect(Collectors.toList());
        sleepingService.saveAllSleeping(sleepingList);
        deactivateMembers(sleepingMembers);
    }

    @Transactional
    public void deactivateMembers(List<Member> memberList) {
        memberList.forEach(Member::deactivate);
    }

    //@Scheduled(cron = "000000")
    public void beforeSleep() {
        List<String> emailLists = memberRepository.getAllBeforeSleepEmails();
        emailLists.forEach(mailService::sendChangeSleep);
    }

    @Transactional(readOnly = true)
    public List<Member> getMembersByLoginId(List<String> loginIdList) {
        return memberRepository.getMembersByLoginIdList(loginIdList);
    }

    @Transactional(readOnly = true)
    public List<LoginLogResponse> getAllLoginLogs() {
        return memberRepository.getAllLoginLogs();
    }

    @Transactional(readOnly = true)
    public List<SearchMemberResponse> searchByMemberName(String name) {
        return memberRepository.getSearchResultsByName(name);
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

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return member;
    }

    private FindResponse issueTokenAndSendMail(String email) {
        String certification = RandomGenerator.getRandomNumber();
        Token findRequestToken = findRequestTokenService.issueToken(email, certification);
        mailService.sendFindCertification(email, certification);
        return FindResponse.from(findRequestToken);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}