package com.FlagHome.backend.domain.reply.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyResponse {
    @Schema(name = "댓글 번호")
    private long id;

    @Schema(name = "작성자")
    private String nickname;

    @Schema(name = "작성자 프로필 사진")
    private String profileImage;

    @Schema(name = "댓글 내용")
    private String content;

    @Schema(name = "작성 시간")
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public ReplyResponse(long id, String nickname, String profileImage, String content, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.content = content;
        this.createdAt = createdAt;
    }
}
