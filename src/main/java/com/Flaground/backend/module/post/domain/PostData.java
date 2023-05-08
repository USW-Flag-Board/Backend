package com.Flaground.backend.module.post.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostData {
    private String title;
    private String content;
    private String boardName;

    @Builder
    public PostData(String title, String content, String boardName) {
        this.title = title;
        this.content = content;
        this.boardName = boardName;
    }
}
