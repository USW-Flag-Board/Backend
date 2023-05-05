package com.FlagHome.backend.module.member.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AvatarResponse {
    private String loginId;

    private String nickName;

    private String bio;

    private String profileImg;

    @QueryProjection
    public AvatarResponse(String loginId, String nickName, String bio, String profileImg) {
        this.loginId = loginId;
        this.nickName = nickName;
        this.bio = bio;
        this.profileImg = profileImg;
    }
}
