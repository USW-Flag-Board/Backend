package com.FlagHome.backend.domain.post.controller.dto.response;

import com.FlagHome.backend.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailResponse {
    @Schema(name = "게시글 번호")
    private Long id;

    @Schema(name = "게시글 제목")
    private String title;

    @Schema(name = "게시글 내용")
    private String content;

    @Schema(name = "게시글 작성자")
    private String author;

    @Schema(name = "게시글 작성시간")
    private LocalDateTime createdAt;

    @Schema(name = "게시글 조회수")
    private int viewCount;

    @Schema(name = "게시글 댓글 수")
    private int replyCount;

    @Schema(name = "게시글 좋아요 수")
    private int likeCount;

    @Schema(name = "게시글 수정 여부")
    private boolean isEdited;

    @Builder
    public PostDetailResponse(Long id, String title, String content, String author, LocalDateTime createdAt,
                              int viewCount, int replyCount, int likeCount, boolean isEdited) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.isEdited = isEdited;
    }

    public static PostDetailResponse from(Post post) {
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getMember().getAvatar().getNickname())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .replyCount(post.getReplyCount())
                .likeCount(post.getLikeCount())
                .isEdited(post.isEdited())
                .build();
    }
}