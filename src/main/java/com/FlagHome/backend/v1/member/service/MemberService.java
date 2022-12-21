package com.FlagHome.backend.v1.member.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.mail.MailService;
import com.FlagHome.backend.v1.member.mail.MailType;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.global.util.RandomNumberGenerator;
import com.FlagHome.backend.global.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    @Transactional
    public void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.USER_ID_EXISTS);
        }
    }

    @Transactional
    public void validateEmail(String email) {
        validateUSWEmail(email);

        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.USER_ID_EXISTS);
        }
    }

    @Transactional
    public void withdrawMember(String password) {
        Member member = memberRepository.findById(SecurityUtils.getMemberId()).get();

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        memberRepository.withdraw(SecurityUtils.getMemberId());
    }

    @Transactional
    public void sendFindIdResult(String name, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        if (!StringUtils.equals(member.getName(), name)) {
            throw new CustomException(ErrorCode.NAME_EMAIL_NOT_MATCH);
        }

        mailService.sendMail(email, MailType.FIND_ID, member.getLoginId());
    }

    @Transactional
    public void sendFindPasswordResult(String email, String newPassword) {
        mailService.sendMail(email, MailType.REISSUE_PASSWORD, newPassword);
    }

    @Transactional
    public String updatePassword(String loginId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        if (!StringUtils.equals(member.getLoginId(), loginId)) {
            throw new CustomException(ErrorCode.ID_EMAIL_NOT_MATCH);
        }

        String newPassword = RandomNumberGenerator.getRandomPassword();
        memberRepository.updatePassword(member.getId(), newPassword);

        return newPassword;
    }

    private void validateUSWEmail(String email) {
        int separateIndex = StringUtils.indexOf(email, "@");
        if (!StringUtils.equals(email.substring(separateIndex), "@suwon.ac.kr")) {
            throw new CustomException(ErrorCode.NOT_USW_EMAIL);
        }
    }
}