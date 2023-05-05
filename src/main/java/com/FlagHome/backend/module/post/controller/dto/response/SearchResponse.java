package com.FlagHome.backend.module.post.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchResponse {
    @Schema(name = "검색 결과")
    private List<PostResponse> searchResults;

    @Schema(name = "검색 건수")
    private int resultCount;

    @Builder
    public SearchResponse(List<PostResponse> searchResults, int resultCount) {
        this.searchResults = searchResults;
        this.resultCount = resultCount;
    }

    public static SearchResponse from(List<PostResponse> searchResults) {
        return SearchResponse.builder()
                .searchResults(searchResults)
                .resultCount(searchResults.size())
                .build();
    }
}
