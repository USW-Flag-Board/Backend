package com.FlagHome.backend.module.post.controller.dto.response;

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

    @Schema(name = "작성자 아이디")
    private String loginId;

    @Schema(name = "작성자 활동명")
    private String nickname;

    @Schema(name = "작성자 프로필 사진")
    private String profileImage;

    @Schema(name = "댓글 내용")
    private String content;

    @Schema(name = "좋아요 응답")
    private LikeResponse like;

    @Schema(name = "댓글 작성 시간")
    private LocalDateTime createdAt;

    @Schema(name = "댓글 수정 여부")
    private boolean isEdited;

    @QueryProjection
    public ReplyResponse(Long id, String loginId, String nickname, String profileImage, String content,
                         LikeResponse like, LocalDateTime createdAt, boolean isEdited) {
        this.id = id;
        this.loginId = loginId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.content = content;
        this.like = like;
        this.createdAt = createdAt;
        this.isEdited = isEdited;
    }
}
