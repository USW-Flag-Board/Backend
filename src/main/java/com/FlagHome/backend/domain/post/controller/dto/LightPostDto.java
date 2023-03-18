package com.FlagHome.backend.domain.post.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LightPostDto {
    private Long id;
    private String title;
    private String boardName;
    private LocalDateTime createdAt;
    private Long viewCount;
    private Integer likeCount;
}
