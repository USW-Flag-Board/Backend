package com.FlagHome.backend.domain.member.service;

import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.activity.memberactivity.service.MemberActivityService;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import com.FlagHome.backend.domain.member.avatar.dto.UpdateAvatarRequest;
import com.FlagHome.backend.domain.member.avatar.service.AvatarService;
import com.FlagHome.backend.domain.member.controller.dto.FindResponse;
import com.FlagHome.backend.domain.member.controller.dto.LoginLogResponse;
import com.FlagHome.backend.domain.member.controller.dto.MemberProfileResponse;
import com.FlagHome.backend.domain.member.dto.SearchMemberResponse;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.member.sleeping.repository.SleepingRepository;
import com.FlagHome.backend.domain.member.sleeping.service.SleepingService;
import com.FlagHome.backend.domain.post.dto.LightPostDto;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.token.entity.Token;
import com.FlagHome.backend.domain.token.service.FindRequestTokenService;
import com.FlagHome.backend.global.common.Status;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.infra.aws.ses.service.MailService;
import com.FlagHome.backend.global.utility.InputValidator;
import com.FlagHome.backend.global.utility.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final SleepingRepository sleepingRepository;
    private final MailService mailService;
    private final AvatarService avatarService;
    private final FindRequestTokenService findRequestTokenService;
    private final PasswordEncoder passwordEncoder;
    private final MemberActivityService memberActivityService;
    private final InputValidator inputValidator;
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
        validateMemberPassword(memberId, password);
        avatarService.deleteAvatar(memberId);
        deleteMemberById(memberId);
    }

    @Transactional
    public void deleteMemberById(long memberId) {
        memberRepository.deleteById(memberId);
    }

    public FindResponse findId(String name, String email) {
        inputValidator.validateUSWEmail(email);

        Member member = findByEmail(email);
        if (!StringUtils.equals(member.getName(), name)) {
            throw new CustomException(ErrorCode.EMAIL_NAME_NOT_MATCH);
        }

        return issueTokenAndSendMail(email);
    }

    public FindResponse findPassword(String loginId, String email) {
        inputValidator.validateUSWEmail(email);
        Member member = findByEmail(email);

        if (!StringUtils.equals(loginId, member.getLoginId())) {
            throw new CustomException(ErrorCode.EMAIL_ID_NOT_MATCH);
        }

        return issueTokenAndSendMail(email);
    }

    public String validateCertification(String email, String certification) {
        Token findRequestToken = findRequestTokenService.findToken(email);
        findRequestToken.validateExpireTime();
        inputValidator.validateCertification(certification, findRequestToken.getValue());
        return findByEmail(email).getLoginId();
    }

    @Transactional // 비밀번호를 잊어서 바꾸는 경우
    public void changePassword(String email, String newPassword) {
        inputValidator.validatePassword(newPassword);
        Member member = findByEmail(email);
        member.updatePassword(newPassword, passwordEncoder);
    }

    @Transactional // 비밀번호를 유저가 변경하는 경우
    public void updatePassword(Long memberId, String currentPassword, String newPassword) {
        Member member = validateMemberPassword(memberId, currentPassword);
        inputValidator.validatePassword(newPassword);

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
        Sleeping sleeping = sleepingRepository.findByLoginId(loginId).orElse(null);
        Member member = findByLoginId(loginId);

        if (sleeping != null) {
            sleepingService.convertSleepToMember(member, sleeping);
        }

        return member;
    }

    public MemberProfileResponse getMemberProfile(String loginId) {
        AvatarResponse avatarResponse = avatarService.getAvatar(loginId);
        List<ParticipateResponse> participateResponseList = memberActivityService.getAllActivitiesOfMember(loginId);
        List<LightPostDto> postList = getMemberPostByLoginId(loginId);

        return MemberProfileResponse.of(avatarResponse, participateResponseList, postList);
    }

    public MyProfileResponse getMyProfile(long memberId) {
        return avatarService.getMyProfile(memberId);
    }

    @Transactional
    public void updateAvatar(long memberId, UpdateAvatarRequest updateAvatarRequest) {
        avatarService.updateAvatar(memberId, updateAvatarRequest);
    }

    @Transactional
    //@Scheduled(cron = "000000")  이후에 설정하기
    public void changeAllToSleepMember() {
        List<Member> sleepingMembers = memberRepository.getAllSleepMembers();
        List<Sleeping> sleepingList = sleepingMembers.stream()
                        .map(Sleeping::of)
                        .collect(Collectors.toList());
        sleepingRepository.saveAll(sleepingList);
        emptyAllMembers(sleepingMembers);
    }

    @Transactional
    public void emptyAllMembers(List<Member> memberList) {
        final Status sleeping = Status.SLEEPING;
        memberList.forEach(member -> member.emptyAndUpdate(sleeping));
    }

    @Transactional
    //@Scheduled(cron = "000000")
    public void beforeSleep() {
        List<String> emailLists = memberRepository.getAllBeforeSleepEmails();
        emailLists.forEach(mailService::sendChangeSleep);
    }

    @Transactional(readOnly = true)
    public List<LightPostDto> getMemberPostByLoginId(String loginId) {
        return postRepository.findMyPostList(loginId);
    }

    @Transactional(readOnly = true)
    public List<Member> getMembersByLoginId(List<String> loginIdList) {
        return memberRepository.getMembersByLoginId(loginIdList);
    }

    public List<LoginLogResponse> getAllLoginLogs() {
        return memberRepository.getAllLoginLogs();
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

    // 수정하기
    public List<SearchMemberResponse> searchByMemberName(String name) {

        List<Member> memberList = memberRepository.findByMemberName(name);
        List<SearchMemberResponse> memberSearchList = new ArrayList<>(memberList.size());
        for (Member member : memberList) {
            memberSearchList.add(
                    SearchMemberResponse.builder()
                            .id(member.getId())
                            .major(member.getMajor())
                            .name(member.getName())
                            .build()
            );
        }

        return memberSearchList;
    }
}