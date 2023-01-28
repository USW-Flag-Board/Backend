package com.FlagHome.backend.domain.member.avatar.repository;

import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepositoryCustom {
    AvatarResponse getAvatar(String loginId);

    MyProfileResponse getMyProfile(long memberId);

    void deleteByMemberId(long memberId);
}
