package com.Flaground.backend.module.report.controller.dto.response;

import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReportResponse {
    private Long reported;
    private Long postId;
    private String board;
    private ReportCategory reportCategory;
    private String detailExplanation;

    @QueryProjection
    public PostReportResponse(Long reported, Long postId, String board, ReportCategory reportCategory,
                              String detailExplanation) {
        this.reported = reported;
        this.postId = postId;
        this.board = board;
        this.reportCategory = reportCategory;
        this.detailExplanation = detailExplanation;
    }
}
