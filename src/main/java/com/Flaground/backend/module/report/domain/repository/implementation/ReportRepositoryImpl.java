package com.Flaground.backend.module.report.domain.repository.implementation;

import com.Flaground.backend.module.report.controller.dto.response.QReportResponse;
import com.Flaground.backend.module.report.controller.dto.response.ReportResponse;
import com.Flaground.backend.module.report.domain.enums.ReportType;
import com.Flaground.backend.module.report.domain.repository.ReportRepositoryCustom;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Flaground.backend.module.report.domain.QReport.report;

@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {
    private final JPQLQueryFactory queryFactory;

    @Override
    public boolean existByIdsAndType(Long reporter, Long reported, ReportType reportType) {
        return queryFactory
                .selectFrom(report)
                .where(report.reporter.eq(reporter),
                        report.reported.eq(reported),
                        report.reportType.eq(reportType))
                .fetchFirst() != null;
    }

    @Override
    public List<ReportResponse> getReports() {
        return queryFactory
                .select(new QReportResponse(
                        report.reported,
                        report.reportType,
                        report.reportCategory,
                        report.detailExplanation,
                        report.createdAt))
                .from(report)
                .fetch();
    }
}
