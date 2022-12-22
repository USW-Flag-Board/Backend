package com.FlagHome.backend.v1.member.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.util.RandomNumberGenerator;
import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.member.dto.UpdatePasswordRequest;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.mail.MailService;
import com.FlagHome.backend.v1.member.mail.MailType;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    public void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.LOGIN_ID_EXISTS);
        }
    }

    public void validateEmail(String email) {
        validateUSWEmail(email);

        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_EXISTS);
        }
    }

    @Transactional
    public void withdraw(Long memberId, String password) {
        validatePassword(memberId, password);

        Member member = memberRepository.findById(memberId).get();
        // dirty checking
        member.updateStatus(Status.WITHDRAW);
    }

    @Transactional
    public void findLoginId(String name, String email) {
        Member member = findMemberByEmail(email);

        if (!StringUtils.equals(member.getName(), name)) {
            throw new CustomException(ErrorCode.NAME_EMAIL_NOT_MATCH);
        }

        sendFindIdResult(member.getLoginId(), email);
    }

    @Transactional
    public void sendFindIdResult(String loginId, String email) {
        mailService.sendMail(email, MailType.FIND_ID, loginId);
    }

    @Transactional
    public void reissuePassword(String loginId, String email) {
        Member member = findMemberByEmail(email);

        if (!StringUtils.equals(member.getLoginId(), loginId)) {
            throw new CustomException(ErrorCode.ID_EMAIL_NOT_MATCH);
        }

        String newPassword = RandomNumberGenerator.getRandomPassword();
        // dirty checking
        member.updatePassword(passwordEncoder.encode(newPassword));
        sendFindPasswordResult(email, newPassword);
    }

    @Transactional
    public void sendFindPasswordResult(String email, String newPassword) {
        mailService.sendMail(email, MailType.REISSUE_PASSWORD, newPassword);
    }

    @Transactional
    public void updatePassword(Long memberId, UpdatePasswordRequest updatePasswordRequest) {
        validatePassword(memberId, updatePasswordRequest.getCurrentPassword());

        Member member = memberRepository.findById(memberId).get();
        if (passwordEncoder.matches(updatePasswordRequest.getNewPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }

        // dirty checking
        member.updatePassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
    }

    private void validatePassword(Long memberId, String password) {
        // 비밀번호 검사는 로그인된 대상만 진행
        Member member = memberRepository.findById(memberId).get();

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateUSWEmail(String email) {
        int separateIndex = StringUtils.indexOf(email, "@");
        if (!StringUtils.equals(email.substring(separateIndex), "@suwon.ac.kr")) {
            throw new CustomException(ErrorCode.NOT_USW_EMAIL);
        }
    }
}