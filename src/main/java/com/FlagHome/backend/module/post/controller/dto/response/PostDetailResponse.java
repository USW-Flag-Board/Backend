package com.FlagHome.backend.module.post.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailResponse {
    @Schema(name = "게시글 번호")
    private Long id;

    @Schema(name = "작성자 아이디")
    private String loginId;

    @Schema(name = "게시글 작성자")
    private String nickname;

    @Schema(name = "작성자 프로필 사진")
    private String profileImage;

    @Schema(name = "게시글 제목")
    private String title;

    @Schema(name = "게시글 내용")
    private String content;

    @Schema(name = "게시글 작성시간")
    private LocalDateTime createdAt;

    @Schema(name = "게시글 조회수")
    private int viewCount;

    @Schema(name = "좋아요 응답")
    private LikeResponse like;

    @Schema(name = "게시글 수정 여부")
    private boolean isEdited;

    @QueryProjection
    public PostDetailResponse(Long id, String loginId, String nickname, String profileImage, String title, String content,
                              LocalDateTime createdAt, int viewCount, LikeResponse like, boolean isEdited) {
        this.id = id;
        this.loginId = loginId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.like = like;
        this.isEdited = isEdited;
    }
}