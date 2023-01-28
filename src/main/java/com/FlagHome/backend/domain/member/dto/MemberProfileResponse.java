package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.member.entity.Member;
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
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @Schema(description = "프로필 이미지", required = true, example = "url")
    private String profileImg;

    @Schema(description = "자기 소개", required = true, example = "안녕하세요?")
    private String bio;

    // 나중에 수정
//    @Schema(description = "참여한 활동", required = true, example = "참여한 활동 DTO")
//    private List<Activity> activityList;

    @Schema(description = "작성한 게시글", required = true, example = "작성한 게시글 DTO")
    private List<PostDto> postList;

    public static MemberProfileResponse of(Member member, List<PostDto> postList) {
        return MemberProfileResponse.builder()
                .loginId(member.getLoginId())
                .postList(postList)
                .build();
    }
}
