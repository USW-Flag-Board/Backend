package com.FlagHome.backend.domain.post.controller.dto;

import com.FlagHome.backend.domain.post.entity.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private int viewCount;
    private int replyCount;
    private int likeCount;
    private boolean isEdited;

    @Builder
    @QueryProjection
    public PostResponse(Long id, String title, String author, LocalDateTime createdAt,
                        int viewCount, int replyCount, int likeCount, boolean isEdited) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
        this.isEdited = isEdited;
    }

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .author(post.getMember().getAvatar().getNickname())
                .createdAt(post.getCreatedAt())
                .viewCount(post.getViewCount())
                .replyCount(post.getReplyList().size())
                .likeCount(post.getLikeCount())
                .isEdited(post.isEdited())
                .build();
    }
}
