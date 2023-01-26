package com.FlagHome.backend.domain.member.avatar.repository;

import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepositoryCustom {
    AvatarResponse getAvatar();
}
