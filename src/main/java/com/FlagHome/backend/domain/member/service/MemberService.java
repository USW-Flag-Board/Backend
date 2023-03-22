package com.FlagHome.backend.domain.member.service;

import com.FlagHome.backend.domain.auth.AuthInformation;
import com.FlagHome.backend.domain.member.Member;
import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import com.FlagHome.backend.domain.member.avatar.service.AvatarService;
import com.FlagHome.backend.domain.member.controller.dto.FindResponse;
import com.FlagHome.backend.domain.member.controller.dto.LoginLogResponse;
import com.FlagHome.backend.domain.member.controller.dto.SearchMemberResponse;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.member.sleeping.service.SleepingService;
import com.FlagHome.backend.domain.token.entity.Token;
import com.FlagHome.backend.domain.token.service.FindRequestTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.infra.aws.ses.service.MailService;
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
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final AvatarService avatarService;
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

    // 멤버 삭제 고민하기 : 보관할 것인가?
    @Transactional
    public void withdraw(Long memberId, String password) {
        validateMemberPassword(memberId, password);
        avatarService.deleteAvatar(memberId);
        deleteMemberById(memberId);
    }

    @Transactional
    public void deleteMemberById(long memberId) {
        memberRepository.deleteById(memberId);
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

    public String validateCertification(String email, String certification) {
        Token findRequestToken = findRequestTokenService.findToken(email);
        findRequestToken.validateExpireTime();
        findRequestToken.validateValue(certification);
        return email;
    }

    @Transactional // 비밀번호를 잊어서 바꾸는 경우
    public void changePassword(String email, String newPassword) {
        Member member = findByEmail(email);
        member.updatePassword(newPassword, passwordEncoder);
    }

    @Transactional // 비밀번호를 유저가 변경하는 경우
    public void updatePassword(Long memberId, String currentPassword, String newPassword) {
        Member member = validateMemberPassword(memberId, currentPassword);

        if (passwordEncoder.matches(newPassword, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }

        member.updatePassword(newPassword, passwordEncoder);
    }

    @Transactional
    public void initMember(AuthInformation authInformation) {
        Member member = memberRepository.save(Member.of(authInformation, passwordEncoder));
        avatarService.initAvatar(member, authInformation.getNickName());
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

    public AvatarResponse getAvatar(String loginId) {
        return avatarService.getAvatar(loginId);
    }

    public MyProfileResponse getMyProfile(long memberId) {
        return avatarService.getMyProfile(memberId);
    }

    @Transactional
    public void updateAvatar(long memberId, Avatar avatar) {
        avatarService.updateAvatar(memberId, avatar);
    }

    @Transactional
    public void updateProfileImage(Long memberId, MultipartFile file) {
        avatarService.updateProfileImage(memberId, file);
    }

    @Transactional
    //@Scheduled(cron = "000000")  이후에 설정하기
    public void changeAllToSleepMember() {
        List<Member> sleepingMembers = memberRepository.getAllSleepMembers();
        List<Sleeping> sleepingList = sleepingMembers.stream()
                        .map(Sleeping::of)
                        .collect(Collectors.toList());
        sleepingService.saveAllSleepings(sleepingList);
        emptyAllMembers(sleepingMembers);
    }

    @Transactional
    public void emptyAllMembers(List<Member> memberList) {
        memberList.forEach(Member::changeToSleep);
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

    private Member validateMemberPassword(Long memberId, String password) {
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