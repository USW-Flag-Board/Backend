package com.FlagHome.backend.domain.post.controller.dto.request;

import com.FlagHome.backend.domain.post.entity.enums.SearchOption;
import com.FlagHome.backend.domain.post.entity.enums.SearchPeriod;
import com.FlagHome.backend.global.annotation.EnumFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchRequest {
    @Schema(name = "게시판 이름")
    @NotBlank
    private String board;

    @Schema(name = "키워드")
    @NotBlank
    private String keyword;

    @Schema(name = "검색 기간", defaultValue = "all")
    @EnumFormat(enumClass = SearchPeriod.class)
    private SearchPeriod period;

    @Schema(name = "검색 옵션")
    @EnumFormat(enumClass = SearchOption.class)
    private SearchOption option;

    @Builder
    public SearchRequest(String board, String keyword, SearchPeriod period, SearchOption option) {
        this.board = board;
        this.keyword = keyword;
        this.period = period;
        this.option = option;
    }
}
