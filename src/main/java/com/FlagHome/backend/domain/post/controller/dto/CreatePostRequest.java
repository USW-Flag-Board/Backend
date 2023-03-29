package com.FlagHome.backend.domain.post.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {
    @Schema(name = "게시글 이름")
    @NotEmpty @NotNull
    private String title;

    @Schema(name = "게시글 내용")
    @NotEmpty @NotNull
    private String content;

    @Schema(name = "게시판 이름", description = "게시글이 작성될 게시판의 이름")
    @NotEmpty @NotNull
    private String boardName;

    @Builder
    public CreatePostRequest(String title, String content, String boardName) {
        this.title = title;
        this.content = content;
        this.boardName = boardName;
    }
}
