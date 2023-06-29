package com.Flaground.backend.module.post.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostData {
    private static final String NOTICE_BOARD = "공지사항";

    private String title;
    private String content;
    private String boardName;
    private List<String> saveImages;
    private List<String> deleteImages;

    @Builder
    public PostData(String title, String content, String boardName, List<String> saveImages, List<String> deleteImages) {
        this.title = title;
        this.content = content;
        this.boardName = boardName;
        this.saveImages = saveImages;
        this.deleteImages = deleteImages;
    }

    public static PostData notice(String title, String content) {
        return PostData.builder()
                .title(title)
                .content(content)
                .boardName(NOTICE_BOARD)
                .saveImages(List.of())
                .deleteImages(List.of())
                .build();
    }
}
