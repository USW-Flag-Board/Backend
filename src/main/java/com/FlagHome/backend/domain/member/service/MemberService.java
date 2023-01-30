package com.FlagHome.backend.domain.member.service;

import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.activity.memberactivity.service.MemberActivityService;
import com.FlagHome.backend.domain.board.enums.SearchType;
import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import com.FlagHome.backend.domain.member.avatar.dto.UpdateAvatarRequest;
import com.FlagHome.backend.domain.member.avatar.service.AvatarService;
import com.FlagHome.backend.domain.member.dto.FindResponse;
import com.FlagHome.backend.domain.member.dto.MemberProfileResponse;
import com.FlagHome.backend.domain.member.dto.UpdatePasswordRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.sleeping.entity.Sleeping;
import com.FlagHome.backend.domain.sleeping.repository.SleepingRepository;
import com.FlagHome.backend.domain.token.entity.Token;
import com.FlagHome.backend.domain.token.service.FindRequestTokenService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.utility.InputValidator;
import com.FlagHome.backend.global.utility.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public FindResponse findId(String email) {
        inputValidator.validateUSWEmail(email);

        if (!memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        FindResponse response = issueTokenAndSendMail(email);
        return response;
    }

    public FindResponse findPassword(String loginId, String email) {
        inputValidator.validateUSWEmail(email);
        Member member = findByEmail(email);

        if (!StringUtils.equals(loginId, member.getLoginId())) {
            throw new CustomException(ErrorCode.EMAIL_ID_NOT_MATCH);
        }

        FindResponse response = issueTokenAndSendMail(email);
        return response;
    }

    public void validateCertification(String email, String certification) {
        Token findRequestToken = findRequestTokenService.findToken(email);
        findRequestToken.validateExpireTime();
        inputValidator.validateCertification(certification, findRequestToken.getValue());
    }

    @Transactional // 비밀번호를 잊어서 바꾸는 경우
    public void changePassword(String email, String newPassword) {
        inputValidator.validatePassword(newPassword);
        Member member = findByEmail(email);
        member.updatePassword(passwordEncoder.encode(newPassword));
    }

    @Transactional // 비밀번호를 유저가 변경하는 경우
    public String updatePassword(Long memberId, UpdatePasswordRequest updatePasswordRequest) {
        inputValidator.validatePassword(updatePasswordRequest.getNewPassword());
        Member member = validateMemberPassword(memberId, updatePasswordRequest.getCurrentPassword());

        if (passwordEncoder.matches(updatePasswordRequest.getNewPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }

        member.updatePassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        return member.getLoginId();
    }

    public MemberProfileResponse getMemberProfile(String loginId) {
        AvatarResponse avatarResponse = avatarService.getAvatar(loginId);
        List<ParticipateResponse> participateResponseList = memberActivityService.getAllActivitiesOfMember(loginId);
        List<PostDto> postList = getMemberPostByLoginId(loginId);

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
                        .map(member -> Sleeping.of(member, passwordEncoder))
                        .collect(Collectors.toList());
        sleepingRepository.saveAll(sleepingList);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getMemberPostByLoginId(String loginId) {
        return postRepository.findBoardWithCondition(null, SearchType.LOGIN_ID, loginId);
    }

    @Transactional(readOnly = true)
    public List<Member> getMembersByLoginId(List<String> loginIdList) {
        return memberRepository.getMembersByLoginId(loginIdList);
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