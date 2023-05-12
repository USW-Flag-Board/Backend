package com.Flaground.backend.module.auth.domain.repository;

import com.Flaground.backend.module.admin.controller.dto.ApproveSignUpResponse;
import com.Flaground.backend.module.auth.domain.AuthInformation;

import java.util.List;

public interface AuthRepositoryCustom {
    List<AuthInformation> getAllNotProceedAuthInformation();

    List<ApproveSignUpResponse> getAllSignUpRequests();
}
