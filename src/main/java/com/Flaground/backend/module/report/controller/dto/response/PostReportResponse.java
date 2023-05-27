package com.Flaground.backend.module.report.controller.dto.response;

import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReportResponse {
    private Long id;
    private Long reported;
    private String board;
    private Long postId;
    private ReportCategory reportCategory;
    private String detailExplanation;

    @QueryProjection
    public PostReportResponse(Long id, Long reported, String board, Long postId,
                              ReportCategory reportCategory, String detailExplanation) {
        this.id = id;
        this.reported = reported;
        this.board = board;
        this.postId = postId;
        this.reportCategory = reportCategory;
        this.detailExplanation = detailExplanation;
    }
}
