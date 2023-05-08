package com.Flaground.backend.module.post.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponse {
    @Schema(name = "게시글 번호")
    private Long id;

    @Schema(name = "게시글 제목")
    private String title;

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

    @Schema(name = "게시글 수정여부")
    private boolean isEdited;

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
}
