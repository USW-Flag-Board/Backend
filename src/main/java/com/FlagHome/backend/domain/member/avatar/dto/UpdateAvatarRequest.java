package com.FlagHome.backend.domain.member.avatar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateAvatarRequest {
    @Schema(name = "닉네임", example = "john")
    private String nickName;

    @Schema(name = "자기 소개", example = "안녕하세요? 백엔드 개발자입니다.")
    private String bio;

    @Builder
    public UpdateAvatarRequest(String nickName, String bio) {
        this.nickName = nickName;
        this.bio = bio;
    }
}
