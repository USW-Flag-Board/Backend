package com.Flaground.backend.module.report;

import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.report.controller.dto.ReportRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    private Member reported;

    @Column
    private String reportedURL;

    @Column
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column
    private String detailReason;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public Report(Long id, Member reporter, Member reported, String reportedURL,
                  ReportType reportType, String detailReason, LocalDateTime createdAt) {
        this.id = id;
        this.reporter = reporter;
        this.reported = reported;
        this.reportedURL = reportedURL;
        this.reportType = reportType;
        this.detailReason = detailReason;
        this.createdAt = createdAt;
    }

    public static Report from(ReportRequest reportRequest) {
        return Report.builder()
                .reportType(reportRequest.getReportType())
                .reportedURL(reportRequest.getReportedUrl())
                .detailReason(reportRequest.getDetailReason())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
