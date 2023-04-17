package com.FlagHome.backend.domain.post.controller.dto.request;

import com.FlagHome.backend.domain.post.entity.enums.SearchPeriod;
import com.FlagHome.backend.domain.post.entity.enums.SearchOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter // @ModelAttribute 때문에
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchRequest {
    @Schema(name = "게시판 이름")
    @NotBlank
    private String boardName;

    @Schema(name = "키워드")
    @NotBlank
    private String keyword;

    @Schema(name = "검색 기간")
    @NotNull
    private SearchPeriod period;

    @Schema(name = "검색 옵션")
    @NotNull
    private SearchOption option;

    @Builder
    public SearchRequest(String boardName, String keyword, SearchPeriod period, SearchOption option) {
        this.boardName = boardName;
        this.keyword = keyword;
        this.period = period;
        this.option = option;
    }
}
