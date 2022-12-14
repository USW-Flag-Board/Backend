package com.FlagHome.backend.domain.member.service;

import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.domain.member.dto.ProfileResponse;
import com.FlagHome.backend.domain.member.dto.UpdatePasswordRequest;
import com.FlagHome.backend.domain.member.dto.UpdateProfileRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.withdrawal.entity.Withdrawal;
import com.FlagHome.backend.domain.withdrawal.repository.WithdrawalRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.utility.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final WithdrawalRepository withdrawalRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void withdraw(Long memberId, String password) {
        validatePassword(memberId, password);
        deleteMemberById(memberId);
    }

    @Transactional
    public void deleteMemberById(long memberId) {
        memberRepository.deleteById(memberId);
    }

    public void checkMemberByEmail(String email) {
        validateUSWEmail(email);

        if (!memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void sendFindIdResult(String email) {
        Member member = findByEmail(email);
        mailService.sendFindIdResult(email, member.getLoginId());
    }

    public void resetPassword(String loginId, String email) {
        validateUSWEmail(email);
        Member member = findByEmail(email);

        if (!StringUtils.equals(member.getLoginId(), loginId)) {
            throw new CustomException(ErrorCode.EMAIL_USER_NOT_MATCH);
        }
    }
    @Transactional
    public void sendNewPassword(String email) {
        Member member = findByEmail(email);
        String newPassword = RandomGenerator.getRandomPassword();

        member.updatePassword(passwordEncoder.encode(newPassword));
        mailService.sendNewPassword(email, newPassword);
    }

    @Transactional
    public String updatePassword(Long memberId, UpdatePasswordRequest updatePasswordRequest) {
        validatePassword(memberId, updatePasswordRequest.getCurrentPassword());

        Member member = findById(memberId);
        if (passwordEncoder.matches(updatePasswordRequest.getNewPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }

        member.updatePassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        return member.getLoginId();
    }

    @Transactional
    public ProfileResponse getProfile(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // ?????? ??? ?????? ??????
        return ProfileResponse.of(member);
    }

    @Transactional
    public String updateProfile(Long memberId, UpdateProfileRequest updateProfileRequest) {
        Member member = findById(memberId);
        member.updateProfile(updateProfileRequest);

        return member.getLoginId();
    }

    @Transactional
    //@Scheduled(cron = "000000")  ????????? ????????????
    public void changeAllToSleepMember(){
        List<Member> sleepingList = memberRepository.getAllSleepMembers();
        sleepingList.forEach(member -> withdrawalRepository.save(Withdrawal.of(member,passwordEncoder)));
    }

    private void validatePassword(Long memberId, String password) {
        // ???????????? ????????? ???????????? ????????? ??????
        Member member = findById(memberId);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    private void validateUSWEmail(String email) {
        int separateIndex = StringUtils.indexOf(email, "@");
        if (!StringUtils.equals(email.substring(separateIndex), "@suwon.ac.kr")) {
            throw new CustomException(ErrorCode.NOT_USW_EMAIL);
        }
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}