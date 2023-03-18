package com.FlagHome.backend.domain.member.avatar.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AvatarResponse {
    private String loginId;

    private String nickName;

    private String bio;

    private String profileImg;

    @Builder
    @QueryProjection
    public AvatarResponse(String loginId, String nickName, String bio, String profileImg) {
        this.loginId = loginId;
        this.nickName = nickName;
        this.bio = bio;
        this.profileImg = profileImg;
    }
}
