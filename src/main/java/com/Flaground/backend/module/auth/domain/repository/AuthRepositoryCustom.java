package com.Flaground.backend.module.auth.domain.repository;

import com.Flaground.backend.module.auth.controller.dto.response.SignUpRequestResponse;
import com.Flaground.backend.module.auth.domain.AuthInformation;

import java.util.List;

public interface AuthRepositoryCustom {
    List<AuthInformation> getAllNotProceedAuthInformation();

    List<SignUpRequestResponse> getSignUpRequests();
}
