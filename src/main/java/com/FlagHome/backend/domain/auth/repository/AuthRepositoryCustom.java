package com.FlagHome.backend.domain.auth.repository;

import com.FlagHome.backend.domain.auth.dto.ApproveSignUpResponse;
import com.FlagHome.backend.domain.auth.entity.AuthInformation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepositoryCustom {
    List<AuthInformation> getAllNotProceedAuthInformation();

    List<ApproveSignUpResponse> getAllNeedApprovalAuthInformation();
}
