package com.FlagHome.backend.domain.admin.service;

import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.member.avatar.service.AvatarService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final AvatarService avatarService;

    @Transactional(readOnly = true)
    public List<AuthInformation> getAllAuthorizedAuthMember() {
        return authRepository.getAllNeedApprovalAuthInformation();
    }

    @Transactional
    public void approveMember(Long authInformationId) {
        Member member = authRepository.findById(authInformationId)
                .map(authInformation -> Member.of(authInformation, passwordEncoder))
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INFORMATION_NOT_FOUND));

        Member savedMember = memberRepository.save(member);
        deleteAuthInformation(authInformationId);
    }

    @Transactional
    public void deleteAuthInformation(Long authInformationId) {
        authRepository.deleteById(authInformationId);
    }

    @Transactional
    public void withdrawMember(Long memberId) {
        memberService.deleteMemberById(memberId);
    }
}
