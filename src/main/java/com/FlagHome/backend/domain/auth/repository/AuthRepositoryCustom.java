package com.FlagHome.backend.domain.auth.repository;

import com.FlagHome.backend.domain.auth.entity.AuthMember;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthRepositoryCustom {
    List<AuthMember> getAllNotProceedAuthMembers();

    List<AuthMember> getAllAuthorizedAuthMembers();

    void deleteAllNotProceedAuthMembers(List<AuthMember> authMemberList);
}
