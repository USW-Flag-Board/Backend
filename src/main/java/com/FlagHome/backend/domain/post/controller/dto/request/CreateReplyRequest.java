package com.FlagHome.backend.domain.post.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReplyRequest {
    @Schema(name = "댓글 내용")
    @NotBlank
    private String content;

    @Builder
    public CreateReplyRequest(String content) {
        this.content = content;
    }
}
