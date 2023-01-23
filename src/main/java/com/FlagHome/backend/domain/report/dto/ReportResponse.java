package com.FlagHome.backend.domain.report.dto;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.report.ReportType;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportResponse {

    private long id;
    //스키마 넣기
    private String reported;

    private ReportType reportType;

    private String detailReason;

    private String url;

    @Builder
    @QueryProjection
    public ReportResponse(long id, String reported, ReportType reportType, String detailReason, String url) {
        this.id = id;
        this.reported = reported;
        this.reportType = reportType;
        this.detailReason = detailReason;
        this.url = url;
    }
}
