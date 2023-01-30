package com.FlagHome.backend.domain.member.avatar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAvatarRequest {
    @Schema(name = "닉네임", example = "john")
    private String nickName;

    @Schema(name = "자기 소개", example = "안녕하세요? 백엔드 개발자입니다.")
    private String bio;

    @Schema(name = "프로필 이미지")
    private String profileImg;
}
