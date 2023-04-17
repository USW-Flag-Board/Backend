package com.FlagHome.backend.domain.post.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyResponse {
    @Schema(name = "댓글 번호")
    private Long id;

    @Schema(name = "댓글 작성자")
    private String nickname;

    @Schema(name = "작성자 프로필 사진")
    private String profileImage;

    @Schema(name = "댓글 내용")
    private String content;

    @Schema(name = "댓글 좋아요 갯수")
    private int likeCount;

    @Schema(name = "댓글 작성 시간")
    private LocalDateTime createdAt;

    @Schema(name = "댓글 수정 여부")
    private boolean isEdited;

    @QueryProjection
    public ReplyResponse(Long id, String nickname, String profileImage, String content,
                         int likeCount, LocalDateTime createdAt, boolean isEdited) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.content = content;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.isEdited = isEdited;
    }
}