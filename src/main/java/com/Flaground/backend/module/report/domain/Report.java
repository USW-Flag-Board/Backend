package com.Flaground.backend.module.report.domain;

import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "reporter_id")
    private Long reporter;

    @Column(name = "reported_id")
    private Long reported;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ReportCategory reportCategory;

    @Column(length = 300)
    private String detailExplanation;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Report(Long reporter, Long reported, ReportType reportType, ReportCategory reportCategory, String detailExplanation) {
        this.reporter = reporter;
        this.reported = reported;
        this.reportType = reportType;
        this.reportCategory = reportCategory;
        this.detailExplanation = detailExplanation;
        this.createdAt = LocalDateTime.now();
    }

    public static Report member(Long reporter, Long reported, ReportData<String> reportData) {
        return Report.builder()
                .reporter(reporter)
                .reported(reported)
                .reportType(reportData.getReportType())
                .reportCategory(reportData.getReportCategory())
                .detailExplanation(reportData.getDetailExplanation())
                .build();
    }

    public static Report content(Long reporter, ReportData<Long> reportData) {
        return Report.builder()
                .reporter(reporter)
                .reported(reportData.getTarget())
                .reportType(reportData.getReportType())
                .reportCategory(reportData.getReportCategory())
                .detailExplanation(reportData.getDetailExplanation())
                .build();
    }
}
