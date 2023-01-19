package com.FlagHome.backend.domain.report.dto;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.report.ReportType;
import com.FlagHome.backend.domain.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

    private long id;
    private Member reporter;
    private Member reported;
    private String reportedURL;
    private ReportType reportType;
    private String detailReason;
    private LocalDateTime createdAt;

    public ReportRequest(Report reportEntity){
        this.id = reportEntity.getId();
        this.reporter = reportEntity.getReporter();
        this.reported = reportEntity.getReported();
        this.reportedURL = reportEntity.getReportedURL();
        this.reportType = reportEntity.getReportType();
        this.detailReason = reportEntity.getDetailReason();
        this.createdAt = reportEntity.getCreatedAt();
    }
}
