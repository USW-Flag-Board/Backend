package com.FlagHome.backend.domain.member.dto;

import com.FlagHome.backend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponse {
    @Schema(description = "아이디", required = true, example = "gmlwh124")
    private String loginId;

    @Schema(description = "프로필 이미지", required = true, example = "url")
    private String profileImg;

    @Schema(description = "자기 소개", required = true, example = "안녕하세요?")
    private String bio;

    // 나중에 수정
//    @Schema(description = "참여한 활동", required = true, example = "참여한 활동 DTO")
//    private List<Activity> activityList;
//
//    @Schema(description = "작성한 게시글", required = true, example = "작성한 게시글 DTO")
//    private List<PostDto> postList;

    public static MyPageResponse of(Member member) {
        return MyPageResponse.builder()
                .loginId(member.getLoginId())
                .profileImg(member.getProfileImg())
                .bio(member.getBio())
                .build();
    }
}
