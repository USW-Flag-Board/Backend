package com.Flaground.backend.module.report.domain;

import com.Flaground.backend.module.report.domain.enums.ReportCategory;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportInfo {
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ReportCategory reportCategory;

    @Column(columnDefinition = "text")
    private String detailExplanation;

    @Builder
    public ReportInfo(ReportType reportType, ReportCategory reportCategory, String detailExplanation) {
        this.reportType = reportType;
        this.reportCategory = reportCategory;
        this.detailExplanation = detailExplanation;
    }

    public static ReportInfo from(AbstractReport<?> abstractReport) {
        return ReportInfo.builder()
                .reportType(abstractReport.getReportType())
                .reportCategory(abstractReport.getReportCategory())
                .detailExplanation(abstractReport.getDetailExplanation())
                .build();
    }
}
