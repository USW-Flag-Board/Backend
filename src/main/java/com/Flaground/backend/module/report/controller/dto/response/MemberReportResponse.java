package com.Flaground.backend.module.report.controller.dto.response;

import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReportResponse {
    private Long reported;
    private String loginId;
    private ReportCategory reportCategory;
    private String detailExplanation;

    @QueryProjection
    public MemberReportResponse(Long reported, String loginId, ReportCategory reportCategory, String detailExplanation) {
        this.reported = reported;
        this.loginId = loginId;
        this.reportCategory = reportCategory;
        this.detailExplanation = detailExplanation;
    }
}
