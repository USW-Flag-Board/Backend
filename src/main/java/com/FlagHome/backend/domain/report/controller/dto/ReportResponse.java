package com.FlagHome.backend.domain.report.controller.dto;

import com.FlagHome.backend.domain.report.ReportType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportResponse {
    private long id;
    private String reported;
    private ReportType reportType;
    private String detailReason;
    private String reportedUrl;

    @Builder
    @QueryProjection
    public ReportResponse(long id, String reported, ReportType reportType, String detailReason, String reportedUrl) {
        this.id = id;
        this.reported = reported;
        this.reportType = reportType;
        this.detailReason = detailReason;
        this.reportedUrl = reportedUrl;
    }
}
