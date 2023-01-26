package com.FlagHome.backend.domain.member.avatar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarResponse {
    private Long id;

    private String loginId;

    private String nickName;

    private String bio;

    private String profileImg;
}
