package com.FlagHome.backend.domain.auth.service;

import com.FlagHome.backend.domain.auth.entity.AuthMember;
import com.FlagHome.backend.domain.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthMemberService {
    private final AuthRepository authRepository;

    /**
     * 매 정오에 재학생 인증 절차를 진행하지 않은 테이블들을 지운다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteNotProceedAuthMember() {
        List<AuthMember> unAuthMemberList = findAllNotProceedAuthMember();
        authRepository.deleteNotProceedAuthMember(unAuthMemberList);
    }

    private List<AuthMember> findAllNotProceedAuthMember() {
        return authRepository.findAllNotProceedAuthMember();
    }
}