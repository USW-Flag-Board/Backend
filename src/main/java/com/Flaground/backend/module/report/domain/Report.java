package com.Flaground.backend.module.report.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "reporter_id")
    private Long reporter;

    @Column(name = "reported_id")
    private Long reported;

    @Embedded
    private ReportInfo reportInfo;

    public Report(Long reporter, Long reported, ReportInfo reportInfo) {
        this.reporter = reporter;
        this.reported = reported;
        this.reportInfo = reportInfo;
    }

    public int getPenalty() {
        return reportInfo.getReportCategory().getPenaltyPoint();
    }
}
