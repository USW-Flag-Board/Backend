package com.FlagHome.backend.domain.member.service;

import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.domain.member.dto.UpdatePasswordRequest;
import com.FlagHome.backend.domain.member.dto.UpdateProfileRequest;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.utility.RandomGenerator;
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

    @Transactional
    public void withdraw(Long memberId, String password) {
        validatePassword(memberId, password);

        memberRepository.deleteById(memberId);
    }

    public void findLoginId(String email) {
        validateUSWEmail(email);

        if (!memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void sendFindIdResult(String email) {
        Member member = findByEmail(email);
        mailService.sendFindIdResult(email, member.getLoginId());
    }

    public void reissuePassword(String loginId, String email) {
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
        // dirty checking
        member.updatePassword(passwordEncoder.encode(newPassword));
        mailService.sendNewPassword(email, newPassword);
    }

    @Transactional
    public void updatePassword(Long memberId, UpdatePasswordRequest updatePasswordRequest) {
        validatePassword(memberId, updatePasswordRequest.getCurrentPassword());

        Member member = findById(memberId);
        if (passwordEncoder.matches(updatePasswordRequest.getNewPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_IS_SAME);
        }

        // dirty checking
        member.updatePassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
    }

    @Transactional
    public void updateProfile(Long id, UpdateProfileRequest updateProfileRequest) {
        memberRepository.updateProfile(id, updateProfileRequest);
    }

    private void validatePassword(Long memberId, String password) {
        // 비밀번호 검사는 로그인된 대상만 진행
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