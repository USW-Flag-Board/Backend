package com.Flaground.backend.module.post.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

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

    @Schema(name = "최종적으로 반영되는 이미지들")
    private List<String> saveImages;

    @Schema(name = "유저의 변심으로 삭제된 이미지들")
    private List<String> deleteImages;

    @Builder
    public PostRequest(String title, String content, String boardName, List<String> saveImages, List<String> deleteImages) {
        this.title = title;
        this.content = content;
        this.boardName = boardName;
        this.saveImages = saveImages;
        this.deleteImages = deleteImages;
    }
}
