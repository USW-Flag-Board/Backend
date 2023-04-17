package com.FlagHome.backend.domain.post.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostRequest {
    @Schema(name = "게시글 이름")
    @NotBlank
    private String title;

    @Schema(name = "게시글 내용")
    @NotBlank
    private String content;

    @Schema(name = "게시판 이름", description = "게시글이 작성될 게시판의 이름")
    @NotBlank
    private String boardName;

    @Builder
    public PostRequest(String title, String content, String boardName) {
        this.title = title;
        this.content = content;
        this.boardName = boardName;
    }
}
