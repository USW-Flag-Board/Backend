package com.Flaground.backend.module.post.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateReplyRequest {
    @Schema(name = "댓글 내용")
    @NotBlank
    private String content;

    public static CreateReplyRequest from(String content) {
        return new CreateReplyRequest(content);
    }
}
