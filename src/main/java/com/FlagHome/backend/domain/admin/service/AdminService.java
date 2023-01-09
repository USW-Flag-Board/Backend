package com.FlagHome.backend.domain.admin.service;

import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.auth.entity.AuthMember;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AuthMember> getAllAuthorizedAuthMember() {
        return authRepository.getAllAuthorizedAuthMembers();
    }

    @Transactional
    public void approveAuthMember(Long authMemberId) {
        Member member = authRepository.findById(authMemberId)
                .map(authMember -> Member.of(authMember, passwordEncoder))
                .orElseThrow(() -> new CustomException(ErrorCode.AUTH_TARGET_NOT_FOUND));

        memberRepository.save(member);
        deleteAuthMember(authMemberId);
    }

    @Transactional
    public void deleteAuthMember(Long authMemberId) {
        authRepository.deleteById(authMemberId);
    }

    @Transactional
    public void withdrawMember(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        member.updateStatus(Status.WITHDRAW);
    }

    @Transactional
    public void banMember(Long memberId) {
        Member member = memberRepository.findById(memberId).get();
        member.updateStatus(Status.BANNED);
    }
}
