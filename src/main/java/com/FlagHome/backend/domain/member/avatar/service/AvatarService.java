package com.FlagHome.backend.domain.member.avatar.service;

import com.FlagHome.backend.domain.member.Member;
import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import com.FlagHome.backend.domain.member.avatar.repository.AvatarRepository;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.global.infra.aws.s3.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private static final String AWS_S3_DIRECTORY = "/avatar";
    private final AvatarRepository avatarRepository;
    private final AwsS3Service awsS3Service;

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
    public void updateProfileImage(Long memberId, MultipartFile file) {
        String profileImageUrl =  awsS3Service.upload(file, AWS_S3_DIRECTORY);
        Avatar avatar = findByMemberId(memberId);
        avatar.changeProfileImage(profileImageUrl);
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
