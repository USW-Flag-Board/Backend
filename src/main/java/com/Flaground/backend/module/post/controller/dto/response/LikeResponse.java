package com.Flaground.backend.module.post.controller.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeResponse {
    private boolean liked;
    private int likeCount;

    @Builder
    @QueryProjection
    public LikeResponse(boolean liked, int likeCount) {
        this.liked = liked;
        this.likeCount = likeCount;
    }

    public static LikeResponse liked(int likeCount) {
        return LikeResponse.builder()
                .liked(true)
                .likeCount(likeCount)
                .build();
    }

    public static LikeResponse disliked(int likeCount) {
        return LikeResponse.builder()
                .liked(false)
                .likeCount(likeCount)
                .build();
    }
}
