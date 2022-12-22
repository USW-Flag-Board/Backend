package com.FlagHome.backend.v1.member.repository;

import com.FlagHome.backend.v1.member.entity.Member;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomMemberRepository {
    void withdraw(Long memberId);

    void updatePassword(Long memberId, String newPassword);
}
