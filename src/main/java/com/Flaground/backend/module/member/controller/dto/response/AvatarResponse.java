package com.Flaground.backend.module.member.controller.dto.response;

import com.Flaground.backend.module.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AvatarResponse {
    private String loginId;

    private String nickname;

    private String bio;

    private String profileImg;

    @Builder
    public AvatarResponse(String loginId, String nickname, String bio, String profileImg) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.bio = bio;
        this.profileImg = profileImg;
    }

    public static AvatarResponse of(Member member) {
        return AvatarResponse.builder()
                .loginId(member.getLoginId())
                .nickname(member.getAvatar().getNickname())
                .bio(member.getAvatar().getBio())
                .profileImg(member.getAvatar().getProfileImage())
                .build();
    }
}
