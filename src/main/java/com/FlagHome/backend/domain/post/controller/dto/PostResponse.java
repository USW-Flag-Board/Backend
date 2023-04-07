package com.FlagHome.backend.domain.post.controller.dto;

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
    private String writer;
    private LocalDateTime createdAt;
    private int viewCount;
    private int replyCount;
    private int likeCount;

    @Builder
    public PostResponse(Long id, String title, String writer, LocalDateTime createdAt, int viewCount, int replyCount, int likeCount) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.replyCount = replyCount;
        this.likeCount = likeCount;
    }
}
