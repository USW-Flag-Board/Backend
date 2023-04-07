package com.FlagHome.backend.domain.reply.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReplyRequest {
    @Schema(name = "게시글 번호")
    @NotNull
    private long postId;

    @Schema(name = "댓글 내용")
    @NotBlank
    private String content;

    @Builder
    public CreateReplyRequest(long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
