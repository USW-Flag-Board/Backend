package com.Flaground.backend.global.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchResponse<T> {
    @Schema(name = "검색 결과")
    private List<T> searchResults;

    @Schema(name = "검색 건수")
    private int resultCount;

    @Builder
    public SearchResponse(List<T> searchResults, int resultCount) {
        this.searchResults = searchResults;
        this.resultCount = resultCount;
    }

    public static <T> SearchResponse<T> from(List<T> searchResults) {
        return SearchResponse.<T>builder()
                .searchResults(searchResults)
                .resultCount(searchResults.size())
                .build();
    }
}
