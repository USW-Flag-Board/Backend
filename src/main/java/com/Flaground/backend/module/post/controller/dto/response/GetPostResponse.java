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
    private List<ReplyResponse> replies;

    @Builder
    public GetPostResponse(PostDetailResponse postDetail, List<ReplyResponse> replies) {
        this.postDetail = postDetail;
        this.replies = replies;
    }

    public static GetPostResponse of(PostDetailResponse postDetailResponse, List<ReplyResponse> replyResponses) {
        return GetPostResponse.builder()
                .postDetail(postDetailResponse)
                .replies(replyResponses)
                .build();
    }
}
