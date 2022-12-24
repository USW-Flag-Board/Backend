package com.FlagHome.backend.domain.member.repository;

import com.FlagHome.backend.domain.member.dto.UpdateProfileRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepositoryCustom {

    void updateProfile(Long memberId, UpdateProfileRequest updateProfileRequest);

}
