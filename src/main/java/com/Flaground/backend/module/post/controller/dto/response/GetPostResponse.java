package com.Flaground.backend.module.post.controller.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPostResponse {
    private PostDetailResponse postDetail;
    private List<String> imageKeys;
    private List<ReplyResponse> replies;

    public GetPostResponse(PostDetailResponse postDetail, List<String> imageKeys, List<ReplyResponse> replies) {
        this.postDetail = postDetail;
        this.imageKeys = imageKeys;
        this.replies = replies;
    }
}
