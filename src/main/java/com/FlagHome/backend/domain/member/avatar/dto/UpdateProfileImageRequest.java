package com.FlagHome.backend.domain.member.avatar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProfileImageRequest {
    @Schema(name = "프로필 사진")
    private MultipartFile profileImage;

    @Builder
    public UpdateProfileImageRequest(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }
}
