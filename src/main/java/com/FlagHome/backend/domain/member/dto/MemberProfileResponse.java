package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.activity.dto.ActivityResponse;
import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.post.dto.LightPostDto;
import com.FlagHome.backend.domain.post.dto.PostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileResponse {
    private AvatarResponse avatarResponse;

    @Schema(description = "참여한 활동", example = "참여한 활동 DTO")
    private List<ParticipateResponse> activityList;

    @Schema(name = "작성한 게시글", example = "작성한 게시글 DTO")
    private List<LightPostDto> postList;

    public static MemberProfileResponse of(AvatarResponse avatarResponse, List<ParticipateResponse> activityList, List<LightPostDto> postList) {
        return MemberProfileResponse.builder()
                .avatarResponse(avatarResponse)
                .activityList(activityList)
                .postList(postList)
                .build();
    }
}
