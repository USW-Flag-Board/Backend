package com.Flaground.backend.module.post.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UpdateReplyRequest {
    @Schema(name = "수정 내용")
    @NotBlank
    private String newContent;

    public static UpdateReplyRequest from(String newContent) {
        return new UpdateReplyRequest(newContent);
    }
}
