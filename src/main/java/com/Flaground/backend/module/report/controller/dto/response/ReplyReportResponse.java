package com.Flaground.backend.module.report.controller.dto.response;


import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyReportResponse {
    private Long id;
    private Long reported;
    private Long postId;
    private Long replyId;
    private ReportCategory reportCategory;
    private String detailExplanation;

    @QueryProjection
    public ReplyReportResponse(Long id, Long reported, Long postId, Long replyId,
                               ReportCategory reportCategory, String detailExplanation) {
        this.id = id;
        this.reported = reported;
        this.postId = postId;
        this.replyId = replyId;
        this.reportCategory = reportCategory;
        this.detailExplanation = detailExplanation;
    }
}
