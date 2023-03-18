package com.FlagHome.backend.domain.admin.service;

import com.FlagHome.backend.domain.auth.controller.dto.ApproveSignUpResponse;
import com.FlagHome.backend.domain.auth.AuthInformation;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import com.FlagHome.backend.domain.member.controller.dto.LoginLogResponse;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberService memberService;
    private final AuthRepository authRepository;

    @Transactional(readOnly = true)
    public List<ApproveSignUpResponse> getAllAuthorizedAuthMember() {
        return authRepository.getAllNeedApprovalAuthInformation();
    }

    @Transactional
    public void approveMember(long authInformationId) {
        AuthInformation authInformation = authRepository.findById(authInformationId)
                        .orElseThrow(() -> new CustomException(ErrorCode.AUTH_INFORMATION_NOT_FOUND));

        memberService.initMember(authInformation);
        deleteAuthInformation(authInformationId);
    }

    @Transactional
    public void deleteAuthInformation(long authInformationId) {
        authRepository.deleteById(authInformationId);
    }

    @Transactional
    public List<LoginLogResponse> viewAllLoginLogs() {
        return memberService.getAllLoginLogs();
    }
}
