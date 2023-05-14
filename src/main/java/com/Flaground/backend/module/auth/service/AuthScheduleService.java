package com.Flaground.backend.module.auth.service;

import com.Flaground.backend.module.auth.domain.AuthInformation;
import com.Flaground.backend.module.auth.domain.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthScheduleService {
    private final AuthRepository authRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNotProceedAuthInformations() {
        List<AuthInformation> notProceedAuthInformations = authRepository.getNotProceedAuthInformations();
        authRepository.deleteAllInBatch(notProceedAuthInformations);
    }
}
