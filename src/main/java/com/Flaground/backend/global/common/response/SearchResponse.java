package com.Flaground.backend.global.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
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

    public SearchResponse(List<T> searchResults) {
        this.searchResults = searchResults;
        this.resultCount = searchResults.size();
    }
}
