package com.Flaground.backend.module.admin.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateNoticeRequest {
    @Schema(name = "게시글 이름")
    @NotBlank
    private String title;

    @Schema(name = "게시글 내용")
    @NotBlank
    private String content;
}
