package com.FlagHome.backend.module.auth.domain.repository;

import com.FlagHome.backend.module.admin.controller.dto.ApproveSignUpResponse;
import com.FlagHome.backend.module.auth.domain.AuthInformation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepositoryCustom {
    List<AuthInformation> getAllNotProceedAuthInformation();

    List<ApproveSignUpResponse> getAllSignUpRequests();
}