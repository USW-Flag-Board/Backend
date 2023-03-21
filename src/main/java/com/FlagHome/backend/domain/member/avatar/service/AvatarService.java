package com.FlagHome.backend.domain.member.avatar.service;

import com.FlagHome.backend.domain.member.Member;
import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import com.FlagHome.backend.domain.member.avatar.repository.AvatarRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;

    @Transactional
    public void initAvatar(Member member, String nickName) {
        avatarRepository.save(Avatar.of(member, nickName));
    }

    @Transactional(readOnly = true)
    public AvatarResponse getAvatar(String loginId) {
        return avatarRepository.getAvatar(loginId);
    }

    @Transactional(readOnly = true)
    public MyProfileResponse getMyProfile(long memberId) {
        return avatarRepository.getMyProfile(memberId);
    }

    @Transactional
    public void updateAvatar(long memberId, Avatar avatar) {
        Avatar findAvatar = findByMemberId(memberId);
        findAvatar.updateAvatar(avatar);
    }

    @Transactional
    public void deleteAvatar(long memberId) {
        avatarRepository.deleteByMemberId(memberId);
    }

    private Avatar findByMemberId(long memberId) {
        return avatarRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
