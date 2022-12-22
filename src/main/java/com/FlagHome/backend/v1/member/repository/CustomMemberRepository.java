package com.FlagHome.backend.v1.member.repository;

import com.FlagHome.backend.v1.member.dto.UpdateProfileRequest;
import com.FlagHome.backend.v1.member.entity.Member;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomMemberRepository {
    void withdraw(Long memberId);

    void updatePassword(Long memberId, String newPassword);

    Member updateProfile(Long memberId, UpdateProfileRequest updateProfileRequest);

}
